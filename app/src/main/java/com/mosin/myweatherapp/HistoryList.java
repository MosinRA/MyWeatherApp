package com.mosin.myweatherapp;

import java.util.LinkedList;

public class HistoryList {
//не пойму что делаю не так, но без статика не работает =(
   private static LinkedList<String> historyList = new LinkedList<>();

   public LinkedList<String> getHistoryList() {
      return historyList;
   }

   public void addHistoryList(String city) {
      historyList.addFirst(city);
   }


}
