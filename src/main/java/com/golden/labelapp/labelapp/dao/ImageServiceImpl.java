package com.golden.labelapp.labelapp.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.models.entities.Labels;
import com.golden.labelapp.labelapp.repositories.ImageRespository;
import com.golden.labelapp.labelapp.repositories.LabelsRepository;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.LabelServices;

/**
 * Implementación de la interfaz ImageServices que proporciona métodos para el manejo de imágenes.
 */
/**
 * This class implements the ImageServices interface and provides the
 * implementation for various image-related operations.
 */
@Service
public class ImageServiceImpl implements ImageServices {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImageRespository imageRepository;

    @Autowired
    private YoloV5Impl yoloV5Impl;

    @Autowired
    private LabelsRepository labelsRepository;
    @Autowired
    private LabelServices labelServices;

    private final String ruta="https://cdn-60ld3n.socialsalert.com/object/image/";

    /**
     * Extrae la información de una imagen en formato JSON y la convierte en un mapa
     * de datos.
     * 
     * @param img La imagen en formato JSON.
     * @return Un mapa de datos que contiene la información extraída de la imagen.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> extractInfoFromJson(Image img) {
        int index = 0;
        Map<String, Object> info_dict = new HashMap<>();
        Map<String, Object> bboxes = new HashMap<>();
        info_dict.put("filename", img.getName());
        info_dict.put("bboxes", bboxes);
        for (Object element : img.getShapes()) {
            Map<String, Object> bbox = new HashMap<>();
            bboxes.put("bbox" + index, bbox);
            String label = (String) ((Map<String, Object>) element).get("label");

            bbox.put("label", label);
            bbox.put("points", ((Map<String, Object>) element).get("points"));
            index++;
        }

        info_dict.put("size", img.getSize());

        return info_dict;
    }

    /**
     * Inserta una imagen en la base de datos.
     * 
     * @param img La imagen a insertar.
     * @return La imagen insertada.
     */
    @Transactional
    @Override
    public Image insertImage(Image img) {
        int id = 0;
        if (imageRepository.findAll().size() > 0) {
            id = imageRepository.findAll().get(imageRepository.findAll().size() - 1).getId() + 1;
        }
        img.setId(id);
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        img.setCreatedAt(date);
        img.setUpdatedAt(now);
        img.setRuta(ruta + "/" + img.getName());

        return imageRepository.save(img);
    }

    /**
     * Sube una o varias imágenes al servidor.
     * 
     * @param file La(s) imagen(es) a subir.
     * @param path La ruta donde se guardarán las imágenes.
     * @return Un mapa de datos que contiene la información de las imágenes subidas.
     */
    @Override
    @SuppressWarnings("null")
    public Map<String, Object> uploadImage(List<MultipartFile> file, String path) {
        List<Map<String, Object>> responses = new ArrayList<>();
        for (MultipartFile f : file) {
            try {
                Path uploatdPath = Paths.get(path).toAbsolutePath().normalize();
                if (!Files.exists(uploatdPath)) {
                    Files.createDirectories(uploatdPath);
                }
                String filename = f.getOriginalFilename();
                Path filepath = uploatdPath.resolve(filename);
                Files.write(filepath, f.getBytes());
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                        .path(filename).toUriString();
                Map<String, Object> res = new HashMap<>();
                res.put("message", "File uploaded successfully");
                res.put("file", filename);
                res.put("url", fileDownloadUri);
                responses.add(res);
            } catch (Exception e) {
                Map<String, Object> res = new HashMap<>();
                res.put("message", "Could not upload the file: " + f.getOriginalFilename() + "!");
                responses.add(res);
                return res;
            }
        }
        return responses.get(0);
    }

    /**
     * Obtiene todas las imágenes almacenadas en la base de datos.
     * 
     * @return Una lista de todas las imágenes almacenadas.
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Image> getPageImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return imageRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    /**
     * Obtiene una imagen por su ID.
     * 
     * @param id El ID de la imagen.
     * @return La imagen encontrada, o un objeto Optional vacío si no se encuentra
     *         la imagen.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Image> getImageById(int id) {
        return Optional.ofNullable(imageRepository.findById(id).get());
    }

    /**
     * Elimina una imagen por su ID.
     * 
     * @param id El ID de la imagen a eliminar.
     */
    @Override
    @Transactional
    public void deleteImage(int id) {
        Image img = imageRepository.findById(id).get();
        int[] idLabel = img.getIds();
        for (int i : idLabel) {
            Labels label = labelsRepository.findById(i).get();
            label.setCant(label.getCant() - 1);
            labelServices.updateLabel(label, i);
        }

        imageRepository.deleteById(id);

    }

    /**
     * Convierte la información de una imagen en formato YOLOv5 y la guarda en la
     * base de datos.
     * 
     * @param info_dict El mapa de datos que contiene la información de la imagen.
     * @param height    La altura de la imagen.
     * @param width     El ancho de la imagen.
     * @param name      El nombre de la imagen.
     * @param labels    La lista de etiquetas disponibles.
     */
    @Transactional
    @SuppressWarnings({ "unused", "unchecked" })
    @Override
    public void convertToYoloV5(Map<String, Object> info_dict, int height, int width, String name, List<Labels> labels) {
        Map<String, Object> bboxes = (Map<String, Object>) info_dict.get("bboxes");
        List<ObjectDetectDto> objlist = new ArrayList<>();
    
        // Iterar sobre las llaves del mapa de bboxes
        for (String key : bboxes.keySet()) {
            Map<String, Object> bbox = (Map<String, Object>) bboxes.get(key);
            List<List<Number>> points = (List<List<Number>>) bbox.get("points");
            List<Object> retval = new ArrayList<>();
    
            // Convertir y normalizar puntos
            for (List<Number> point : points) {
                double xValue = point.get(0).doubleValue();
                double yValue = point.get(1).doubleValue();
                
                double x = Math.round(xValue / width * 100000.0) / 100000.0;
                double y = Math.round(yValue / height * 100000.0) / 100000.0;
                
                List<Double> pointList = new ArrayList<>();
                pointList.add(x);
                pointList.add(y);
                retval.add(pointList);
            }
    
            // Asegurar que bbox contenga la clave "label" y que sea un String
            if (bbox.containsKey("label") && bbox.get("label") instanceof String) {
                String labelName = (String) bbox.get("label");
    
                // Buscar la etiqueta correspondiente en la lista de labels
                boolean labelFound = false;
                for (Labels label : labels) {
                    if (label.getLabel().equals(labelName) && label.getCant() >= 13){
                        ObjectDetectDto obj = new ObjectDetectDto(label.getId(), retval, label.getLabel());
                        objlist.add(obj);
                        labelFound = true;
                        break; // Salir del bucle si se encuentra la etiqueta
                    }
                }

            } else {
                // Manejar el caso donde la etiqueta no está presente o no es un String
                throw new IllegalArgumentException("Etiqueta no válida en bbox: " + bbox);
            }
        }
    
        // Guardar los resultados convertidos
        yoloV5Impl.saveYoloV5(name, objlist);
    }

    /**
     * Actualiza una imagen en la base de datos.
     * 
     * @param img La imagen actualizada.
     * @param id  El ID de la imagen a actualizar.
     * @return La imagen actualizada, o un objeto Optional vacío si no se encuentra
     *         la imagen.
     */
    @Override
    @Transactional
    public Optional<Image> updateImage(Image img, int id) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            Update update = new Update();
            update.set("name", img.getName());
            update.set("height", img.getHeight());
            update.set("width", img.getWidth());
            update.set("shapes", img.getShapes());
            update.set("ruta", img.getRuta());
            update.set("ids", img.getIds());
            LocalDateTime now = LocalDateTime.now();
            update.set("updatedAt", now);
            Query query = Query.query(Criteria.where("_id").is(id));
            Image updatedImage = mongoTemplate.findAndModify(query, update, Image.class);
            for (int i : img.getIds()) {
                Labels label = labelsRepository.findById(i).get();
                label.setCant(label.getCant() + 1);
                labelsRepository.save(label);
            }
            if (updatedImage != null) {
                return Optional.of(updatedImage);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImageByCreatedDate(Date startDate, Date endDate) {
        return imageRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImageByUpdatedDate(Date startDate, Date endDate) {
        return imageRepository.findByUpdatedAtBetween(startDate, endDate);
    }

    @Override
    public Optional<Image> getLastId() {
        return imageRepository.findFirstByOrderByIdDesc();
    }
}
