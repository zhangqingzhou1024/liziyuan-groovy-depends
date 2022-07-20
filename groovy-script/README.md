## groovy-nifi 样例：

### 1、接口取值放值  
```groovy
import org.apache.commons.io.IOUtils
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.liziyuan.groovy.utils.HttpClient
import com.liziyuan.groovy.bean.Product
import com.liziyuan.groovy.bean.ProductFlow
import java.nio.charset.StandardCharsets
import org.apache.nifi.flowfile.attributes.CoreAttributes


String PRODUCT_URL = "http://81.70.0.124:8888/server/products"
String PRODUCT_FLOW_URL = "http://81.70.0.124:8888/server/products/%s/flows"


def oriFlowFile = session.get()
if (!oriFlowFile){
	return
} 
    

String jsonStr = ""
try{
    session.read(oriFlowFile, {inputStream ->
        jsonStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    } as InputStreamCallback)
} catch (e) {
    log.error('read oriFlowFile error: {},{}', [jsonStr, e] as Object[])
    session.remove(oriFlowFile)
    return
}
log.info(jsonStr)
session.remove(oriFlowFile)


String request = HttpClient.sendGetRequest(PRODUCT_URL, null);
List<Product> products = JSONArray.parseArray(request, Product.class);
println("products-size : " + products.size());

for(Product item:products ){
	String resultStr = JSONObject.toJSONString(item)
	def flowFile = session.create()
	flowFile = session.write(flowFile, {outputStream ->
	outputStream.write(resultStr.getBytes(StandardCharsets.UTF_8))} as OutputStreamCallback)
	String currentFlowUrl = String.format(PRODUCT_FLOW_URL, item.getId());
	String flows = HttpClient.sendGetRequest(currentFlowUrl, null);
	List<ProductFlow> ProductFlows = JSONArray.parseArray(flows, ProductFlow.class);
    println("product-flows-size : " + ProductFlows.size());
	session.putAttribute(flowFile, "productId", item.getId())
   
	session.transfer(flowFile, REL_SUCCESS)
}

```