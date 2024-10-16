package com.fx.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class Data {
  @JsonProperty("GBP")
  public GBP gBP;

  @JsonProperty("INR")
  public INR iNR;
}
