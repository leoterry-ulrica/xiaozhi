package com.dist.bdf.base.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成代码的方法。
 * @author 何顺
 * @create 2014年11月20日
 */
public class CodeUtil {

    /**
     * 生成分页查询的代码
     * @author 何顺
     * @param obj 对象
     * @param entityName 实体名称
     * @param entitySymbol 实体代号，比如 select form User u中的u。
     * @param pageVariableName 方法参数中的分页变量名称
     * @param entityVariableName 方法参数中的实体变量名称
     * @param domainName domain的名称
     * @return
     */
    public static String genericPageCode(Object obj, String entityName, String entitySymbol, String pageVariableName,
            String entityVariableName, String domainName) {
        List<Field> fields = new ArrayList<Field>();
        ReflectionUtil.getFieldList(obj, fields);
        StringBuffer code = new StringBuffer();
        code.append("StringBuffer hql = new StringBuffer();hql.append(\"from " + entityName + " " + entitySymbol
                + " where 1=1\");");
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                Class<?> type = field.getType();
                String fieldName = field.getName();
                String methodName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1)
                        .toUpperCase());
                String methodStr = entityVariableName + ".get" + methodName + "()";

                if (type.toString().indexOf("boolean") != -1) {
                    methodStr = methodStr.replace(".get", ".is");
                    code.append("if(null !=" + methodStr + "){");
                    code.append(" hql.append(\" and " + entitySymbol + "." + fieldName + " like '%\" +" + methodStr
                            + "+\"%'\");");
                    code.append("}");
                }
                if (type.toString().indexOf("String") != -1) {
                    code.append("if(!StringUtils.isBlank(" + methodStr + ")){");
                    code.append(" hql.append(\" and " + entitySymbol + "." + fieldName + " like '%\" +" + methodStr
                            + "+\"%'\");");
                    code.append("}");
                }

                if (type.toString().indexOf("Date") != -1) {
                    code.append("if(null != " + methodStr + "){");
                    code.append(" hql.append(\" and " + fieldName + " >= to_date(\"+DateUtil.toDateStr(" + methodStr
                            + ")+\"','yyyy-mm-dd'\");");
                    code.append("}");
                }
            }

        }
        code.append(" return " + domainName + ".query(" + pageVariableName + ".getPageNo(), " + pageVariableName
                + ".getPageSize(), hql.toString());");
        return code.toString();
    }

}
