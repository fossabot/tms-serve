package com.odakota.tms.system.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
public class AmazonConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

//    @Bean
//    public AmazonS3 awsS3Client() {
//        return AmazonS3ClientBuilder.standard()
//                                    .withRegion(Regions.fromName(region))
//                                    .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials()))
//                                    .build();
//    }

    @Bean
    public AmazonSNS amazonSnsClient() {
        return AmazonSNSClientBuilder.standard()
                                     .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials()))
                                     .withRegion(Regions.fromName(region)).build();
    }
}
