package com.dsp.bios.migrationupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dsp.bios.util.ConstantsUtil;
import com.dsp.bios.util.FolderCreationUtils;
import com.dsp.bios.util.UserUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UploadService {
    @Autowired
    private MigrateFilesLsTempRepository repo;
    @Autowired
    private MigrateFilesRsTempRepository repoRs;

    @Autowired
    private FolderCreationUtils folderCreation;
    @Autowired
    private UserUtils utils;
    @Value("${service.url}")
    private String openKmUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpServletRequest httpRequest;

    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public List<ResponseUuids> uploadToDsp2(String moduleName, String locale) {
        String house = getHouseAbbr();
        if(ConstantsUtil.LOK_SABHA.equalsIgnoreCase(house))
        return uploadToDsp2Ls(moduleName, locale, house);
        else if(ConstantsUtil.RAJYA_SABHA.equalsIgnoreCase(house))
        return uploadToDsp2Rs(moduleName, locale, house);

        return null;

    }

    public List<ResponseUuids> uploadToDsp2Ls(String moduleName, String locale, String house) {
        // TODO Auto-generated method stub
        List<ResponseUuids> responseUuids = new ArrayList<>();
        Specification<MigrateFilesLsTemp> spec = Specification.where(hasModuleName(moduleName));
        spec = spec.and(hasFileUuid());
        spec = spec.and(hasLocale(locale));
        spec = spec.and(hasHouse(house));
        List<MigrateFilesLsTemp> entitys = repo.findAll(spec);
        System.out.println("entity");
        System.out.println(entitys.size());
        for (MigrateFilesLsTemp res : entitys) {
            System.out.println("file " + res.getFileUrl());
            if (res.getFileUuid() != null) {
                continue;
            }
            ResponseUuids responseFromUpload = fetchAndUploadFile(res, new MigrateFilesRsTemp(), house);
            System.out.println(responseFromUpload);
            if (responseFromUpload != null && responseFromUpload.getUploadedUuid() != null) {
                responseUuids.add(responseFromUpload);
                continue;
            } else
                continue;
        }
        return responseUuids;
    }

    // rs
    public List<ResponseUuids> uploadToDsp2Rs(String moduleName, String locale, String house) {
        // TODO Auto-generated method stub
        List<ResponseUuids> responseUuids = new ArrayList<>();
        Specification<MigrateFilesRsTemp> spec = Specification.where(hasModuleNameRs(moduleName));
        spec = spec.and(hasFileUuidRs());
        spec = spec.and(hasLocaleRs(locale));
        spec = spec.and(hasHouseRs(house));
        List<MigrateFilesRsTemp> entitys = repoRs.findAll(spec);
        System.out.println("entity");
        System.out.println(entitys.size());
        for (MigrateFilesRsTemp res : entitys) {
            System.out.println("file " + res.getFileUrl());
            if (res.getFileUuid() != null) {
                continue;
            }
            ResponseUuids responseFromUpload = fetchAndUploadFile(new MigrateFilesLsTemp(), res, house);
            System.out.println(responseFromUpload);
            if (responseFromUpload != null && responseFromUpload.getUploadedUuid() != null) {
                responseUuids.add(responseFromUpload);
                continue;
            } else
                continue;
        }
        return responseUuids;
    }

    private ResponseUuids fetchAndUploadFile(MigrateFilesLsTemp response, MigrateFilesRsTemp responseRs, String house) {
        // TODO Auto-generated method stub

        // logger.info("Processing document: {}", response.getFileUrl());
        byte[] fileData = null;
        try {
            if (ConstantsUtil.LOK_SABHA.equalsIgnoreCase(house))
                fileData = restTemplate.getForObject(response.getFileUrl(), byte[].class);
            else if (ConstantsUtil.RAJYA_SABHA.equalsIgnoreCase(house))
                fileData = restTemplate.getForObject(responseRs.getFileUrl(), byte[].class);

            // System.out.println("fileData " + fileData);
        } catch (Exception e) {
            System.out.println("ERROR While Fetching data from URl " + e.getMessage());
            logger.warn("Incorrect URL or failed to fetch: {}", response.getFileUrl(), e);
            if (ConstantsUtil.LOK_SABHA.equalsIgnoreCase(house))
                return mapDataToResp(updateNonUuid(response));
            else if (ConstantsUtil.RAJYA_SABHA.equalsIgnoreCase(house))
                return mapDataToRespRs(updateNonUuidRs(responseRs));

        }
        // logger.info("Processing document: {}", response.getFileUrl());
        if (fileData == null || fileData.length == 0) {
            // logger.warn("File data is null for URL: {}", response.getFileUrl());
            if (ConstantsUtil.LOK_SABHA.equalsIgnoreCase(house))
                return mapDataToResp(updateNonUuid(response));
            else if (ConstantsUtil.RAJYA_SABHA.equalsIgnoreCase(house))
                return mapDataToRespRs(updateNonUuidRs(responseRs));

        }
        // System.out.println("data " + fileData);
        ResponseUuids uuid = uploadAndReturnUuid(response, responseRs, fileData, house);
        if (uuid != null && uuid.getUploadedUuid() != null) {
            return uuid;
        }
        // logger.warn("Upload failed or UUID is empty for document: {}", response.getId());
        return null;
    }

    private ResponseUuids uploadAndReturnUuid(MigrateFilesLsTemp response, MigrateFilesRsTemp responseRs,
            byte[] fileData, String house) {
        // TODO Auto-generated method stub
        if (ConstantsUtil.LOK_SABHA.equalsIgnoreCase(house)) {
            response = uploadAndReturnUuidLs(response,
                    fileData);
            if (response == null) {
                return null;
            }
            return mapDataToResp(response);
        } else if (ConstantsUtil.RAJYA_SABHA.equalsIgnoreCase(house)) {
            responseRs = uploadAndReturnUuidRs(responseRs,
                    fileData);
            if (responseRs == null) {
                return null;
            }
            return mapDataToRespRs(responseRs);
        }
        return null;
    }

    public String getHouseAbbr() {
        Long house = utils.getHouseId();
        // System.out.println(repo.getHouseById(house));
        return repo.getHouseById(house);
    }

    public String getTokenFromRequest() {

        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public String getFileNameWithOutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }

    public MigrateFilesLsTemp uploadAndReturnUuidLs(MigrateFilesLsTemp response,
            byte[] fileData) {
        try {
            String houseAbbr = getHouseAbbr();
            System.out.println("response " + response);
            String uploadUrl = openKmUrl + "/document/upload";
            if (fileData == null) {
                return updateNonUuid(response);

            }
            // // Set Authorization Token
            String token = getTokenFromRequest();
            if (token == null || token.isEmpty()) {
                logger.error("Authorization token is missing. Cannot upload file.");
                return null;
            }

            List<String> folders = new ArrayList();
            folders.add(houseAbbr);

            folders.add(response.getModuleName());
            folders.add(response.getFileType());

            System.out.println("folders " + folders);
            String rootPath = ConstantsUtil.OKM_ROOT;
            String folderPath = folderCreation.createDmsFolderStructure(rootPath, folders);
            System.out.println("folder path " + folderPath);
            HttpHeaders headers = new HttpHeaders();
            System.out.println("uploadUrl  " + uploadUrl);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + token);

            String originalFileName = response.getFileName();
            System.out.println("Original file name: " + originalFileName);
            boolean alreadyUploaded = repo.existsByModuleNameAndFileNameAndFileUuidIsNotNull(
                    response.getModuleName(),
                    originalFileName);
            System.out.println("already uploaded " + alreadyUploaded);
            // **CHANGE 2: Check if document already exists before uploading**
            if (alreadyUploaded) {
                logger.info("File already exists in OpenKM, skipping upload: {}", originalFileName);
                return updateUploadedData(response, response.getFileUuid()); // mark as already present
            }
            String fileNameWithoutExt = getFileNameWithOutExtension(response.getFileName());
            System.out.println("file Name before slash and space update" + fileNameWithoutExt);
            if (fileNameWithoutExt.contains("/") || fileNameWithoutExt.contains(" ")) {
                fileNameWithoutExt = fileNameWithoutExt
                        .replace(" ", "-")
                        .replace("/", "-");
            }
            // System.out.println("file Name after slash and space update" +
            // fileNameWithoutExt);
            String updatedFileName = fileNameWithoutExt + "_dsp1.pdf";
            // Create a temporary file
            File tempFile = File.createTempFile(updatedFileName, ".pdf");
            System.out.println("updatedFileName file with " + updatedFileName);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileData);
            }
            // Create file resource
            ByteArrayResource resource = new ByteArrayResource(fileData) {
                @Override
                public String getFilename() {
                    return updatedFileName;
                }
            };

            // Prepare request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            body.add("folderPath", folderPath);
            body.add("documentName", updatedFileName);
            body.add("masterId", 231);

            body.add("applicationNo", response.getFileType() + "_" + updatedFileName);
            body.add("referenceId", 1);
            body.add("referenceType", "ApplicationInstance");
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            System.out.println("uploading file with");
            System.out.println(requestEntity);
            System.out.println(uploadUrl);
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uploadUrl, HttpMethod.POST,
                    requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            logger.info("Response from OpenKM: {}", responseEntity);
            // Check response status
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                Map<String, Object> responseBody = responseEntity.getBody();
                logger.info("Response Body: {}", responseBody);
                // Extract "data"
                Object dataObject = responseBody.get("data");
                if (dataObject instanceof Map) {
                    Map<String, Object> dataMap = (Map<String, Object>) dataObject;
                    // Extract "documentUuid"
                    Object generatedUuid = dataMap.get("documentUuid");
                    logger.info("Extracted documentUuid: {}", generatedUuid);
                    if (generatedUuid != null && !generatedUuid.toString().trim().isEmpty()) {
                        String uuidString = generatedUuid.toString().trim();
                        try {
                            UUID uuid = UUID.fromString(uuidString);
                            System.out.println("before update " + response.getId());
                            response = updateUploadedData(response, "" + uuid);
                            logger.info("entity after saving");
                            System.out.println("after update " + response);
                            return response;
                        } catch (IllegalArgumentException ex) {
                            logger.error("Invalid UUID format received from OpenKM: {}", uuidString, ex);
                        }
                    } else {
                        logger.warn("Received empty or null UUID from OpenKM: {}", dataMap);
                    }
                } else {
                    logger.warn("Response does not contain 'data' field: {}", responseBody);
                }
            } else {

                logger.error("Failed to upload document. HTTP Status: {}", responseEntity.getStatusCode());
                return response;
            }
        } catch (IOException e) {
            logger.error("Error writing temp file: ", e);
        } catch (Exception e) {
            logger.error("Error uploading file: ", e.getMessage());
        }
        return null;
    }

    public MigrateFilesRsTemp uploadAndReturnUuidRs(MigrateFilesRsTemp response,
            byte[] fileData) {
        try {
            String houseAbbr = getHouseAbbr();
            System.out.println("response " + response);
            String uploadUrl = openKmUrl + "/document/upload";
            if (fileData == null) {
                return updateNonUuidRs(response);
            }
            // // Set Authorization Token
            String token = getTokenFromRequest();
            if (token == null || token.isEmpty()) {
                logger.error("Authorization token is missing. Cannot upload file.");
                return null;
            }
            List<String> folders = new ArrayList();
            folders.add(houseAbbr);
            folders.add(response.getModuleName());
            folders.add(response.getFileType());
            System.out.println("folders " + folders);
            String rootPath = ConstantsUtil.OKM_ROOT;
            String folderPath = folderCreation.createDmsFolderStructure(rootPath, folders);
            System.out.println("folder path " + folderPath);
            HttpHeaders headers = new HttpHeaders();
            System.out.println("uploadUrl  " + uploadUrl);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + token);

            String originalFileName = response.getFileName();
            System.out.println("Original file name: " + originalFileName);
            boolean alreadyUploaded = repoRs.existsByModuleNameAndFileNameAndFileUuidIsNotNull(
                    response.getModuleName(),
                    originalFileName);
            System.out.println("already uploaded " + alreadyUploaded);
            // **CHANGE 2: Check if document already exists before uploading**
            if (alreadyUploaded) {
                logger.info("File already exists in OpenKM, skipping upload: {}", originalFileName);
                return updateUploadedDataRs(response, response.getFileUuid()); // mark as already present
            }
            String fileNameWithoutExt = getFileNameWithOutExtension(response.getFileName());
            System.out.println("file Name before slash and space update" + fileNameWithoutExt);
            if (fileNameWithoutExt.contains("/") || fileNameWithoutExt.contains(" ")) {
                fileNameWithoutExt = fileNameWithoutExt
                        .replace(" ", "-")
                        .replace("/", "-");
            }
            // System.out.println("file Name after slash and space update" +
            // fileNameWithoutExt);
            String updatedFileName = fileNameWithoutExt + "_dsp1.pdf";
            // Create a temporary file
            File tempFile = File.createTempFile(updatedFileName, ".pdf");
            System.out.println("updatedFileName file with " + updatedFileName);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileData);
            }
            // Create file resource
            ByteArrayResource resource = new ByteArrayResource(fileData) {
                @Override
                public String getFilename() {
                    return updatedFileName;
                }
            };

            // Prepare request body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            body.add("folderPath", folderPath);
            body.add("documentName", updatedFileName);
            body.add("masterId", 231);

            body.add("applicationNo", response.getFileType() + "_" + updatedFileName);
            body.add("referenceId", 1);
            body.add("referenceType", "ApplicationInstance");
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            System.out.println("uploading file with");
            System.out.println(requestEntity);
            System.out.println(uploadUrl);
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(uploadUrl, HttpMethod.POST,
                    requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            logger.info("Response from OpenKM: {}", responseEntity);
            // Check response status
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                Map<String, Object> responseBody = responseEntity.getBody();
                logger.info("Response Body: {}", responseBody);
                // Extract "data"
                Object dataObject = responseBody.get("data");
                if (dataObject instanceof Map) {
                    Map<String, Object> dataMap = (Map<String, Object>) dataObject;
                    // Extract "documentUuid"
                    Object generatedUuid = dataMap.get("documentUuid");
                    logger.info("Extracted documentUuid: {}", generatedUuid);
                    if (generatedUuid != null && !generatedUuid.toString().trim().isEmpty()) {
                        String uuidString = generatedUuid.toString().trim();
                        try {
                            UUID uuid = UUID.fromString(uuidString);
                            System.out.println("before update " + response.getId());
                            response = updateUploadedDataRs(response, "" + uuid);
                            logger.info("entity after saving");
                            System.out.println("after update " + response);
                            return response;
                        } catch (IllegalArgumentException ex) {
                            logger.error("Invalid UUID format received from OpenKM: {}", uuidString, ex);
                        }
                    } else {
                        logger.warn("Received empty or null UUID from OpenKM: {}", dataMap);
                    }
                } else {
                    logger.warn("Response does not contain 'data' field: {}", responseBody);
                }
            } else {

                logger.error("Failed to upload document. HTTP Status: {}", responseEntity.getStatusCode());
                return response;
            }
        } catch (IOException e) {
            logger.error("Error writing temp file: ", e);
        } catch (Exception e) {
            logger.error("Error uploading file: ", e.getMessage());
        }
        return null;
    }

    private MigrateFilesLsTemp updateUploadedData(MigrateFilesLsTemp response, String uuid) {

        System.out.println("id " + response.getId());
        Optional<MigrateFilesLsTemp> entity = repo
                .findByIdAndModuleNameAndFileUuidIsNull(response.getId(), response.getModuleName());

        if (entity.isPresent()) {
            MigrateFilesLsTemp entitys = entity.get();
            System.out.println("entity " + entitys);

            entitys.setFileUuid(uuid);
            entitys.setIsUpdatedUuid(true);
            entitys = repo.save(entitys);
            System.out.println("entity " + entitys);
            return entitys;
        } else {
            Optional<MigrateFilesLsTemp> exists = repo
                    .findByIdAndModuleName(response.getId(), response.getModuleName());
            if (exists.isPresent()) {
                return exists.get();
            }
        }

        return response;
    }

    private MigrateFilesRsTemp updateUploadedDataRs(MigrateFilesRsTemp response, String uuid) {

        System.out.println("id " + response.getId());
        Optional<MigrateFilesRsTemp> entity = repoRs
                .findByIdAndModuleNameAndFileUuidIsNull(response.getId(), response.getModuleName());

        if (entity.isPresent()) {
            MigrateFilesRsTemp entitys = entity.get();
            System.out.println("entity " + entitys);

            entitys.setFileUuid(uuid);
            entitys.setIsUpdatedUuid(true);
            entitys = repoRs.save(entitys);
            System.out.println("entity " + entitys);
            return entitys;
        } else {
            Optional<MigrateFilesRsTemp> exists = repoRs
                    .findByIdAndModuleName(response.getId(), response.getModuleName());
            if (exists.isPresent()) {
                return exists.get();
            }
        }

        return response;
    }

    private ResponseUuids mapDataToResp(MigrateFilesLsTemp respo) {

        return ResponseUuids.builder()
                .uploadedFileUrl(respo.getFileUrl())
                .moduleName(respo.getModuleName())
                .uploadedUuid(respo.getFileUuid())
                .build();
    }

    private ResponseUuids mapDataToRespRs(MigrateFilesRsTemp respo) {

        return ResponseUuids.builder()
                .uploadedFileUrl(respo.getFileUrl())
                .moduleName(respo.getModuleName())
                .uploadedUuid(respo.getFileUuid())
                .build();
    }

    private MigrateFilesLsTemp updateNonUuid(MigrateFilesLsTemp response) {
        Optional<MigrateFilesLsTemp> entity = repo
                .findByIdAndModuleNameAndFileUuidIsNull(response.getId(), response.getModuleName());
        if (entity.isPresent()) {
            MigrateFilesLsTemp entitys = entity.get();
            entitys.setIsUpdatedUuid(false);
            entitys = repo.save(entitys);
            System.out.println("entity " + entity);
            return entitys;
        }
        Optional<MigrateFilesLsTemp> exists = repo
                .findByIdAndModuleName(response.getId(), response.getModuleName());
        if (exists.isPresent()) {
            return exists.get();
        }
        return response;
    }

    private MigrateFilesRsTemp updateNonUuidRs(MigrateFilesRsTemp response) {
        Optional<MigrateFilesRsTemp> entity = repoRs
                .findByIdAndModuleNameAndFileUuidIsNull(response.getId(), response.getModuleName());
        if (entity.isPresent()) {
            MigrateFilesRsTemp entitys = entity.get();
            entitys.setIsUpdatedUuid(false);
            entitys = repoRs.save(entitys);
            System.out.println("entity " + entity);
            return entitys;
        }
        Optional<MigrateFilesRsTemp> exists = repoRs
                .findByIdAndModuleName(response.getId(), response.getModuleName());
        if (exists.isPresent()) {
            return exists.get();
        }
        return response;
    }

    public static Specification<MigrateFilesLsTemp> hasModuleName(String appName) {
        return (root, query, criteriaBuilder) -> {
            if (appName != null) {
                return criteriaBuilder.equal(root.get("moduleName"), appName);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    private Specification<MigrateFilesLsTemp> hasFileUuid() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("fileUuid"));
    }

    public static Specification<MigrateFilesLsTemp> hasLocale(String locale) {
        return (root, query, criteriaBuilder) -> {
            if (locale != null) {
                return criteriaBuilder.equal(root.get("locale"), locale);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    public static Specification<MigrateFilesLsTemp> hasHouse(String house) {
        return (root, query, criteriaBuilder) -> {
            if (house != null) {
                return criteriaBuilder.equal(root.get("house"), house);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    public static Specification<MigrateFilesRsTemp> hasModuleNameRs(String appName) {
        return (root, query, criteriaBuilder) -> {
            if (appName != null) {
                return criteriaBuilder.equal(root.get("moduleName"), appName);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    private Specification<MigrateFilesRsTemp> hasFileUuidRs() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("fileUuid"));
    }

    public static Specification<MigrateFilesRsTemp> hasLocaleRs(String locale) {
        return (root, query, criteriaBuilder) -> {
            if (locale != null) {
                return criteriaBuilder.equal(root.get("locale"), locale);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    public static Specification<MigrateFilesRsTemp> hasHouseRs(String house) {
        return (root, query, criteriaBuilder) -> {
            if (house != null) {
                return criteriaBuilder.equal(root.get("house"), house);
            }
            return criteriaBuilder.conjunction(); // Returns a valid Predicate that does not filter anything
        };
    }

    // fileUuid

}
