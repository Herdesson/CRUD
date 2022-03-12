package org.example.SpringBootEssencies.Util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataUtil {
    public String formatLocationTimeToDatabaseStyle(LocalDateTime ldt){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ldt);
    }
}
