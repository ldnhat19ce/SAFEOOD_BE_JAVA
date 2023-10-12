package tech.dut.safefood.configurator;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${app.s3.aws.credentials.accessKey}")
    private String s3AccessKey;

    @Value("${app.s3.aws.credentials.secretKey}")
    private String s3SecretKey;

    @Value("${app.s3.aws.region.static}")
    private String s3Region;

    public AWSCredentialsProvider credential() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(this.s3AccessKey, this.s3SecretKey);
        return new AWSStaticCredentialsProvider(basicAWSCredentials);
    }

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard().withCredentials(credential()).withRegion(this.s3Region).build();
    }
}
