package com.liziyuan.groovy.bean;

/**
 * @author zqz
 * @date 2022-03 21:17
 */
public class ProductFlow {
    Long productId;
    Long flowId;
    String flowName;
    String flowDesc;

    public ProductFlow(Long productId, Long flowId, String flowName, String flowDesc) {
        this.productId = productId;
        this.flowId = flowId;
        this.flowName = flowName;
        this.flowDesc = flowDesc;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    @Override
    public String toString() {
        return "ProductFlow{" +
                "productId=" + productId +
                ", flowId=" + flowId +
                ", flowName='" + flowName + '\'' +
                ", flowDesc='" + flowDesc + '\'' +
                '}';
    }
}
