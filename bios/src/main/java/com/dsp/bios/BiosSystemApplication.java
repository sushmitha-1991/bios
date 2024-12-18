/**
 * 
 * @author 
 * @version 
 */
 
package com.dsp.bios;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BiosSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiosSystemApplication.class, args);
    }

}