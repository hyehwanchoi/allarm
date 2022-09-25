package com.nike.allarm.service;

import com.nike.allarm.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CrawlerService {

    public Set<Element> nikeCraw() {
        String url = "https://www.nike.com/kr/launch/?type=upcoming&activeDate=date-filter:AFTER";
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements elements = document.select("[data-active-date]");

        return elements.stream().filter(element -> element.text().contains("응모 시작") && !element.text().contains("THE DRAW")).collect(Collectors.toSet());
    }

    public Set<Item> getApplyItem(Set<Element> elements) {
        Set<Item> items = new HashSet<>();
        Iterator iterator = elements.iterator();

        Vector<String> duplicate = new Vector<>();
        while(iterator.hasNext()) {
            Element element = (Element) iterator.next();
            LocalDateTime date = LocalDateTime.parse(element.attr("data-active-date"), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
            Element subElement = element.selectFirst("[class=card-link d-sm-b comingsoon]");

            if(subElement != null) {
                String title = subElement.attr("title");
                boolean duplication = false;
                for (String name : duplicate) {
                    if(title.equals(name)) {
                        duplication = true;
                        break;
                    }
                }

                duplicate.add(title);
                String urls = "https://www.nike.com" + subElement.attr("href");
                String img = element.selectFirst("img").attr("data-src");

                String caption = "";
                try {
                    caption = element.selectFirst("[class=figcaption-content]").text();
                } catch (NullPointerException e) {
                    e.getMessage();
                }

                items.add(new Item(title, date, urls, img, caption));
            }
        }

        return items;
    }

    public void dailyMorningAlarm() {
        Set<Element> elements = nikeCraw();
        Set<Item> items = getApplyItem(elements);

        LocalDateTime localDateTime = LocalDateTime.now();
        String today = localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("=====오늘은 "+today+" 입니다================================");
        items.stream().filter(item -> isApply(item.getStartDate()))
                .forEach(item -> System.out.println("응모 시작 날짜 : " + item.getStartDate() +
                        "\n상품명 : " + item.getTitle() +
                        "\nurl : " + item.getWebUrl()));
        System.out.println("========================================================");
    }

    private boolean isApply(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        return !localDateTime.isEqual(now);
    }
}
