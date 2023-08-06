package com.exmple.task.entity;

public enum EStatus {
  STATUS_ACTIVE("Active"),
  STATUS_INACTIVE("Inactive");

  private final String status;

  EStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString(){
    return status;
  }
}
