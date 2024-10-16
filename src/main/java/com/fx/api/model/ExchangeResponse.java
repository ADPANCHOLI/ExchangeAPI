package com.fx.api.model;

public class ExchangeResponse {
  public Meta meta;
  public Data data;

  public Meta getMeta() {
    return meta;
  }

  public Data getData() {
    return data;
  }

  public ExchangeResponse(Meta meta, Data data) {
    this.meta = meta;
    this.data = data;
  }

  @Override
  public String toString() {
    return "ExchangeResponse{" + "meta=" + meta + ", data=" + data + '}';
  }
}
