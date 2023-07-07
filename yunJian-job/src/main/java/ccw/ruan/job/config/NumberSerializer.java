package ccw.ruan.job.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author 陈翔
 */
public class NumberSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if ("java.math.BigDecimal".equalsIgnoreCase(fieldType.getTypeName())){
            BigDecimal value = (BigDecimal) object;

            out.write(value.toPlainString());
        }

    }
}
