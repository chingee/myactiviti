package activiti.graph.spring;

import java.io.InputStream;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class MyHistoryProcessInstanceDiagramCmd implements Command<InputStream> {
    protected String historyProcessInstanceId;
    
    public MyHistoryProcessInstanceDiagramCmd(String historyProcessInstanceId) {
        this.historyProcessInstanceId = historyProcessInstanceId;
    }

    public InputStream execute(CommandContext commandContext) {
        try {
            CustomProcessDiagramGenerator customProcessDiagramGenerator = new CustomProcessDiagramGenerator();

            return customProcessDiagramGenerator
                    .generateDiagram(historyProcessInstanceId);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

