package matt.hornung;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {

    static String bucketName = "bucket-name";
    static String fileName = "file.json";

    public static void main(String[] args) throws Exception {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();

        listBuckets(s3);
        uploadFile(s3);
        readObject(s3);
    }

    static void listBuckets(AmazonS3 s3) {
        s3.listBuckets().forEach(bucket -> System.out.println(bucket.getName()));
    }

    static void uploadFile(AmazonS3 s3) throws  Exception {
        File myFile = generateJsonFile();
        s3.putObject(bucketName, fileName, myFile);
    }

    static File generateJsonFile() throws Exception {
        File file = File.createTempFile("aws-s3-","json");
        Path path = file.getAbsoluteFile().toPath();
        Files.writeString(path, "{\"key\":\"value2\"}");
        return file;
    }

    static void readObject(AmazonS3 s3) throws Exception {
        S3Object o = s3.getObject(bucketName, fileName);
        S3ObjectInputStream s3is = o.getObjectContent();

        System.out.println(new String(s3is.readAllBytes(), StandardCharsets.UTF_8));
    }
}
