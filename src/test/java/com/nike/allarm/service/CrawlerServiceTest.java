package com.nike.allarm.service;

import com.nike.allarm.domain.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

class CrawlerServiceTest {

    @Test
    void nikeCrawTest() throws IOException {
        String url = "https://www.nike.com/kr/launch/?type=upcoming&activeDate=date-filter:AFTER";
        Document document = Jsoup.connect(url).get();

        Elements elements = document.select("[data-active-date]");
        System.out.println(elements.toString());

        for (Element element : elements) {
            String text = element.text();
            System.out.println(text);
            System.out.println(text.contains("응모 시작"));
        }

        List<Element> items = elements.stream().filter(element -> element.text().contains("응모 시작") && !element.text().contains("THE DRAW")).collect(Collectors.toList());
    }

    @Test
    void test() throws IOException {
        String url = "https://www.nike.com/kr/launch/?type=upcoming&activeDate=date-filter:AFTER";
        Document document = Jsoup.connect(url).get();

        Elements elements = document.select("[data-active-date]");

        List<Item> items = new ArrayList<>();
        Vector<String> duplicate = new Vector<>();
        for (Element element : elements) {
            LocalDateTime date = LocalDateTime.parse(element.attr("data-active-date"), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
            Element subElement = element.selectFirst("[class=card-link d-sm-b comingsoon]");
            if(subElement != null) {
                String name = subElement.attr("title");

                boolean duplication = false;
                for (String title : duplicate) {
                    if(title.equals(name)) {
                        duplication = true;
                        break;
                    }
                }

                duplicate.add(name);
                if(!duplication) {
                    String urls = "https://www.nike.com" + subElement.attr("href");
                    String image = element.selectFirst("img").attr("data-src");

                    String caption = "";
                    try {
                        caption = element.selectFirst("[class=figcaption-content]").text();
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }

                    LocalDateTime localDateTime = LocalDateTime.now();

                    String applyDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    String today = localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//                    System.out.println(applyDate);
//                    System.out.println(today);
//                    System.out.println(applyDate.equals(today));

                    items.add(new Item(name, date, urls, image, caption));
                }
            }
        }

        System.out.println(items.size());
    }
}