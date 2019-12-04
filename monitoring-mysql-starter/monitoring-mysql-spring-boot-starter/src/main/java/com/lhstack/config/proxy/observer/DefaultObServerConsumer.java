package com.lhstack.config.proxy.observer;

import com.mysql.cj.BindValue;
import com.mysql.cj.QueryBindings;
import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 默认ObServer接口实现类
 */
public class DefaultObServerConsumer extends AbstractObServerConsumer implements ObServerConsumer {

    @Override
    public Object apply(ObServer obServer) throws Exception {
        Object apply = null;
        try{
            long createTime = System.currentTimeMillis();
            apply = obServer.apply();
            printInfo(createTime);
        }catch (Exception e){
            throw new Exception(e);
        }finally {

        }
        return apply;
    }

    private void printInfo(long time) throws SQLException {
        System.out.println("sql execute time as " + (time - System.currentTimeMillis()) + " ms");
        Statement statement = (Statement) metaData.get("statement");
        if(statement instanceof ClientPreparedStatement){
            printClientPreparedStatement((ClientPreparedStatement) statement);
        }else if(statement.isWrapperFor(ClientPreparedStatement.class)){
            printClientPreparedStatement(statement.unwrap(ClientPreparedStatement.class));
        }
    }

    private void printClientPreparedStatement(ClientPreparedStatement statement) throws SQLException {
        String sql = statement.asSql();
        System.out.println("execute sql as ==>" + sql);
        String pre = statement.getPreparedSql();
        System.out.println("pre sql as ==>" + pre);
        QueryBindings<?> queryBindings = statement.getQueryBindings();
        BindValue[] bindValue = queryBindings.getBindValues();
        for (int i = 0; i < bindValue.length; i++) {
            System.out.println(new String(bindValue[i].getByteValue()));
        }
    }

}
