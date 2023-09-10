package io.github.comicmq;

import io.github.comicmq.service.EsSearchService;
import io.github.comicmq.service.impl.EsSearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ComicmqApplicationTests {

    @Autowired
    private EsSearchService esSearchService;

    @Test
    void contextLoads() throws IOException {


    }

}
