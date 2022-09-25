package com.nike.allarm.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.nike.allarm.client.NikeWebClientFactory;
import com.nike.allarm.domain.Item;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplyService {

  private NikeWebClientFactory nikeWebClientFactory;

  public void apply(List<Item> items) {
    items.forEach(item -> applyPersonal("chh9975@naver.com", "gpghkschl12#", item.getWebUrl()));
  }

  private void applyPersonal(String id, String password, String url) {
    WebClient webClient = nikeWebClientFactory.getWebClient();
    try {
      HtmlPage page = webClient.getPage("https://www.nike.com/kr//ko_kr/login?successUrl="+url);
      HtmlTextInput idInput = page.getFirstByXPath("//*[@id=\"j_password\"]");
      HtmlTextInput pwdInput = page.getFirstByXPath("//*[@id=\"j_username\"]");
      HtmlButton loginButton = page.getFirstByXPath("/html/body/section/section/div/div/div[2]/div/div[2]/div/button");

      idInput.setTextContent(id);
      pwdInput.setTextContent(password);
      HtmlPage productPage = loginButton.click();
      HtmlInput checkBox = productPage.getFirstByXPath("//*[@id=\"infoAgree\"]");
      checkBox.isChecked();

      HtmlInput sizeInput = productPage.getFirstByXPath("/html/body/div[1]/div/div[1]/div[2]/div[1]/section/div[2]/aside/div[2]/div/div/div/div/form/div/input");
      HtmlSelect sizeList = productPage.getFirstByXPath("//*[@id=\"selectSize\"]");

      HtmlOption option = null;
      for (int i = 0; i < sizeList.getOptionSize(); i++) {
        if(sizeList.getOption(i).getAttribute("data-value").equals("265") ||
           sizeList.getOption(i).getAttribute("data-value").equals("240")) {
          option = sizeList.getOption(i);
        }
      }

      sizeInput.setAttribute("value", option.getAttribute("data-skuid"));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

//  val webClient = NikeWebClientFactory.getWebClient()
//  val page: HtmlPage = webClient.getPage("${baseUrl}/ko_kr/login?successUrl=${successUrl}")
//
//  val idInput: HtmlTextInput = page.getFirstByXPath("//*[@id=\"j_username\"]")
//  val pwdInput: HtmlPasswordInput = page.getFirstByXPath("//*[@id=\"j_password\"]")
//  val loginButton: HtmlButton =
//          page.getFirstByXPath("/html/body/section/section/div/div/div[2]/div/div[2]/div/button")
//
//  idInput.text = id
//  pwdInput.text = password
//
//  val productPage: HtmlPage = loginButton.click()
//
//  // TODO 테스트 필요
//  val checkBox: HtmlInput = productPage.getFirstByXPath("//*[@id=\"infoAgree\"]")
//  checkBox.isChecked = true
//
//  val sizeInput: HtmlInput =
//          productPage.getFirstByXPath("/html/body/div[1]/div/div[1]/div[2]/div[1]/section/div[2]/aside/div[2]/div/div/div/div/form/div/input")
//  val sizeList: HtmlSelect = productPage.getFirstByXPath("//*[@id=\"selectSize\"]")
//
//  // Option 선택
//  val option =
//          sizeList.options.find { it.getAttribute("data-value") == "265" || it.getAttribute("data-value") == "240" }
//                ?: sizeList.getOption(3)
//
//                        // 선택한 옵션으로 Form 데이터 채움
//                        sizeInput.setAttribute("value", option.getAttribute("data-skuid"))
//
//  val selectedPage: HtmlPage = sizeList.setSelectedAttribute(option, true)
//
//  val button: HtmlAnchor = selectedPage.getFirstByXPath("//*[@id=\"btn-buy\"]")
//  val appliedPage: HtmlPage = button.click()
//
//          webClient.close()
}
