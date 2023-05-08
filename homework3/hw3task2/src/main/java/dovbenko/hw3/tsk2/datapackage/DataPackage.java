package dovbenko.hw3.tsk2.datapackage;

import dovbenko.hw3.tsk2.node.Node;

public class DataPackage {
    private final Node destinationNode;

    private final String data;

    private final long startTime;

    public DataPackage(Node destinationNode, String data) {
        this.destinationNode = destinationNode;
        this.data = data;

        // Фиксируется время, когда создаётся пакет данных. Необходимо для
        // вычисления времени доставки до узла назначения.
        startTime = System.nanoTime();
    }

    @Override
    public String toString() {
        return "DataPackage{" +
                "destinationNode=" + destinationNode.getNodeId() +
                ", data='" + data + '\'' +
                ", startTime=" + startTime +
                '}';
    }

    public Node getDestinationNode() {
        return this.destinationNode;
    }

    public long getStartTime() {
        return 0;
    }

    public String getData(){
        return data;
    }
}
