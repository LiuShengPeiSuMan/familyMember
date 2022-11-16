package com.liushengpei.controller;

import com.liushengpei.pojo.Examine;
import com.liushengpei.service.IExamineService;
import com.liushengpei.util.resultutil.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 待审核
 */
@RestController
@RequestMapping(value = "/examine")
public class ExamineController {

    @Autowired
    private IExamineService examineService;

    /**
     * 查询所有审核记录
     */
    @PostMapping(value = "/getAllExamine")
    public Result<List<Examine>> getAllExamine() {
        List<Examine> examines = examineService.examineAll();
        return Result.success(examines);
    }

    /**
     * 审核的详细信息
     */
    @PostMapping(value = "/examineDetailed")
    public Result<Object> examineDetailed(@RequestParam(value = "id", defaultValue = "", required = false) String id,
                                          @RequestParam(value = "type", defaultValue = "", required = false) Integer type) {

        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (type == null) {
            return Result.fail("类型不能为空");
        }
        Object data = examineService.detailedData(id, type);
        return Result.success(data);
    }

    /**
     * 修改审核结果
     */
    @PostMapping(value = "/findingsOfAudit")
    public Result<String> findingsOfAudit(@RequestParam(value = "status", defaultValue = "", required = false) Integer status,
                                          @RequestParam(value = "id", defaultValue = "", required = false) String id,
                                          @RequestParam(value = "type", defaultValue = "", required = false) Integer type,
                                          @RequestParam(value = "examineUser", defaultValue = "", required = false) String examineUser) {
        if (status == null) {
            return Result.fail("审核结果不能为空");
        }
        if (id == null || id.equals("")) {
            return Result.fail("id不能为空");
        }
        if (type == null) {
            return Result.fail("审核类型为空");
        }
        Integer integer = examineService.updateResult(status, id, type, examineUser);
        if (integer == 0) {
            return Result.fail("失败");
        }
        return Result.success("完成");
    }

    /**
     * 查看历史记录
     */
    @PostMapping(value = "/examineRecord")
    public Result<List<Examine>> examineRecord(@RequestParam(value = "status", defaultValue = "", required = false) Integer status) {
        List<Examine> record = examineService.Record(status);
        return Result.success(record);
    }

    /**
     * 删除历史记录
     */
    @PostMapping(value = "/deleteExamine")
    public Result<String> deleteExamine(@RequestParam(value = "ids", defaultValue = "", required = false) List<String> ids) {
        if (ids.isEmpty()) {
            Result.fail("id不能为空");
        }
        String data = examineService.delExamine(ids);
        return Result.success(data);
    }
}
