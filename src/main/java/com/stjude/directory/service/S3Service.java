package com.stjude.directory.service;

import com.stjude.directory.utils.StringOps;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    private static final String bucketName = "st-jude-photos"; // Replace with your actual bucket name//todo - move this to properties file


    public S3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        // Generate a unique file name for the image
        String fileName = "family-photos/"+ file.getOriginalFilename();

        // Create PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType()) // Important to set content type
                .build();

        // Upload the image to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Return the file name
        return fileName;
    }

    public String uploadHighLightImage(MultipartFile file) throws IOException {
        // Generate a unique file name for the image
        String fileName = "highlights/"+ file.getOriginalFilename();

        // Create PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType()) // Important to set content type
                .build();

        // Upload the image to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Return the file name
        return fileName;
    }

    // Upload image to a custom S3 key (used for per-year uploads)
    public String uploadImageWithCustomKey(MultipartFile file) throws IOException {
        String fileName = "church-photos/" + "_" + file.getOriginalFilename();

        // Create PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType()) // Important to set content type
                .build();

        // Upload the image to S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        // Return the file name
        return fileName;
    }

    public String generatePresignedUrl(String fileName) {
        // Create GetObjectRequest for the file
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        // Create the presign request to generate a pre-signed URL valid for 24 hours
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofHours(24))
                .build();

        // Generate the pre-signed URL
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    public String generatePublicUrl(String fileName) {
        // Set the object ACL to public-read
        s3Client.putObjectAcl(PutObjectAclRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build());

        // Construct the public URL
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    public void deleteFileFromS3(String key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );
    }

    // List up to 'limit' image keys for a given year
    public List<String> listImageKeysByYear(int year, int limit) {
        String prefix = "family-photos/" + year + "/";
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .maxKeys(limit)
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
        List<String> keys = new ArrayList<>();
        for (S3Object obj : listRes.contents()) {
            keys.add(obj.key());
        }
        return keys;
    }

    // List all image keys for a given year (for pagination)
    public List<String> listAllImageKeysByYear(int year) {
        String prefix = "family-photos/" + year + "/";
        List<String> keys = new ArrayList<>();
        String continuationToken = null;
        do {
            ListObjectsV2Request.Builder reqBuilder = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .maxKeys(1000);
            if (continuationToken != null) {
                reqBuilder.continuationToken(continuationToken);
            }
            ListObjectsV2Response res = s3Client.listObjectsV2(reqBuilder.build());
            for (S3Object obj : res.contents()) {
                keys.add(obj.key());
            }
            continuationToken = res.nextContinuationToken();
        } while (continuationToken != null);
        return keys;
    }

}
