package com.liziyuan.groovy

import com.alibaba.fastjson.JSONArray
import com.liziyuan.groovy.demo1.bean.Product
import com.liziyuan.groovy.demo1.bean.ProductFlow

class TestHttp {
    /**
     * request urls
     */
    private static String PRODUCT_URL = "http://81.70.0.124:8888/server/products"
    private static String PRODUCT_FLOW_URL = "http://81.70.0.124:8888/server/products/%s/flows"

    static void main(String[] args) {
        String request = HttpClient.sendGetRequest(PRODUCT_URL, null);
        List<Product> products = JSONArray.parseArray(request, Product.class);
        println("products-size : " + products.size());
        products.forEach(item -> {
            println(item);
            String currentFlowUrl = String.format(PRODUCT_FLOW_URL, item.getId());
            String flows = HttpClient.sendGetRequest(currentFlowUrl, null);
            List<ProductFlow> ProductFlows = JSONArray.parseArray(flows, ProductFlow.class);
            ProductFlows.forEach(e -> {

                println(e);
            })
        }

        )

    }
}
