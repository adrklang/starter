package com.lhstack.config.proxy.abstracts;

import com.lhstack.config.proxy.observer.ObServerConsumer;

import java.sql.*;

public abstract class AbstractProxyStatement implements Statement {

    protected Statement statement;

    /**
     * obServerConsumer对象，用于操作obServer接口执行sql语句，并获取其他信息
     */
    protected ObServerConsumer obServerConsumer;

    public AbstractProxyStatement(Statement statement) {
        this.statement = statement;
        /**
         * 获取用户实现的ObServerConsumer接口类，不然使用默认实现
         */
        try {
            obServerConsumer = (ObServerConsumer) Class.forName(System.getProperty("proxy.observer.class","com.lhstack.config.proxy.observer.DefaultObServerConsumer")).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws SQLException {
        this.statement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.statement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        this.statement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.statement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        this.statement.setMaxFieldSize(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.statement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.statement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        this.statement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        this.statement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.statement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.statement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        this.statement.setCursorName(name);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.statement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.statement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.statement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        this.statement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.statement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        this.statement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.statement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.statement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        this.statement.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        this.statement.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return this.statement.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.statement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return this.statement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.statement.getGeneratedKeys();
    }


    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.statement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        this.statement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.statement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.statement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.statement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.statement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.statement.isWrapperFor(iface);
    }
}
