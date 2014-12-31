package org.yy.framework.base.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * 
 * @author  zhouliang
 * @version  [版本号, 2013年11月12日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CustomTimeSerializer extends JsonSerializer<Date> {
    /** {@inheritDoc} */
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
        throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(value);
        gen.writeString(formattedDate);
    }
}