package com.nike.allarm.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class Item {

  private final String title;
  private final LocalDateTime startDate;
  private final String webUrl;
  private final String img;
  private final String caption;
}
