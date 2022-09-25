package com.nike.allarm.scheduler;

import com.nike.allarm.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NikeScheduler {

  @Autowired
  private final CrawlerService crawlerService;

  // 1시간에 1번씩 상품 파싱
//  @Scheduled(initialDelay = 1000 * 3, fixedDelay = 1000 * 60 * 60)
//  public void getEventItem() {
//    List<Element> elements = crawlerService.nikeCraw();
//    List<Item> items = crawlerService.getApplyItem();
//
//    for (int i = 0; i < items.size(); i++) {
//      log.info(items.get(i).toString());
//    }
//  }

  @Scheduled(cron = "0 50 9 * * *", zone = "Asia/Seoul")
//  @Scheduled(initialDelay = 1000, fixedDelay = 1000)
  public void getDailyMorningAlarm() {
    crawlerService.dailyMorningAlarm();
  }
}
