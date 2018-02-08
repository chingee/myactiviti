package activiti.override.factory;

import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.interceptor.Session;

public class MyDbSqlSessionFactory extends DbSqlSessionFactory {
	
	@Override
	public Session openSession() {
		return new MySqlSession(this);
	}
}
