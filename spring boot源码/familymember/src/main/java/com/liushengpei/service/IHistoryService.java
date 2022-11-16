package com.liushengpei.service;

import com.liushengpei.pojo.Examine;

import java.util.List;

/**
 * 户主审核记录
 */
public interface IHistoryService {

    List<Examine> examineHistory(String name);
}
