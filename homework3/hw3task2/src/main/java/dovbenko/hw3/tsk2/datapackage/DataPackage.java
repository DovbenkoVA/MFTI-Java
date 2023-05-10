package dovbenko.hw3.tsk2.datapackage;

import dovbenko.hw3.tsk2.node.Node;

public final class DataPackage {
    private final Node destinationNode;
    private final String data;
    private final long startTime;
    private long endTime;

    public DataPackage(Node destinationNode, String data) {
        this.destinationNode = destinationNode;
        this.data = data;
        this.startTime = System.nanoTime();

    }

    @Override
    public String toString() {
        return "DataPackage{"
                + "destinationNode=" + destinationNode.getNodeId()
                + ", data='" + data + '\''
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + '}';
    }

    public Node getDestinationNode() {
        return this.destinationNode;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getData() {
        return data;
    }
}
