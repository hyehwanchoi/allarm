package com.nike.allarm.client;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

public class NikeWebClientFactory {

  WebClient webClient = null;

  public WebClient getWebClient() {
    webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7);
    webClient.setCssEnabled(false);
    webClient.setJavaScriptEnabled(true);
    webClient.setRedirectEnabled(true);
    webClient.setThrowExceptionOnFailingStatusCode(false);
    webClient.setThrowExceptionOnScriptError(false);
    webClient.setAjaxController(new NicelyResynchronizingAjaxController());

    return webClient;
  }
}
