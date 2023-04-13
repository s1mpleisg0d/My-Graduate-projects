package q2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Flow {
    private int flowId;
    private double flowWeight;

    public Flow(int flowId, double flowWeight) {
        this.flowId = flowId;
        this.flowWeight = flowWeight;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public void setFlowWeight(double flowWeight) {
        this.flowWeight = flowWeight;
    }

    public int getFlowId() {
        return this.flowId;
    }

    public double getFlowWeight() {
        return this.flowWeight;
    }
}

class Packet {
    private double arrivalTime;
    private Flow packetFlow;
    private double len;

    public Packet(double arrivalTime, Flow packetFlow, double len) {
        this.arrivalTime = arrivalTime;
        this.packetFlow = packetFlow;
        this.len = len;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public Flow getPacketFlow() {
        return this.packetFlow;
    }

    public double getLen() {
        return this.len;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPacketFlow(Flow packetFlow) {
        this.packetFlow = packetFlow;
    }

    public void setLen(double len) {
        this.len = len;
    }

    @Override
    public String toString() {
        return "Packet arrTime " + getArrivalTime() + " flow id " + getPacketFlow().getFlowId() + " w " + getPacketFlow().getFlowWeight() + " packet length " + getLen();
    }
}

class Event {
    private String eventType;
    private double n;
    private Packet eventPacket;
    private double startTime;
    private double endTime;

    public Event(String eventType, double n, Packet eventPacket, double startTime, double endTime) {
        this.eventType = eventType;
        this.n = n;
        this.eventPacket = eventPacket;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEventType() {
        return this.eventType;
    }

    public double getN() {
        return this.n;
    }

    public Packet getEventPacket() {
        return this.eventPacket;
    }

    public double getStartTime() {
        return this.startTime;
    }

    public double getEndTime() {
        return this.endTime;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setN(double n) {
        this.n = n;
    }

    public void setEventPacket(Packet eventPacket) {
        this.eventPacket = eventPacket;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Event{type=" + getEventType() + ", t=" + getN() + ", p=Packet{flowId=" + getEventPacket().getPacketFlow().getFlowId() + ", arrTime=" + getEventPacket().getArrivalTime() + ", length=" + getEventPacket().getLen() + ", virtualStartTime=" + getStartTime() + ", virtualFinishTime=" + getEndTime() + "}}";
    }
}

public class WF2Q {

    public static List<String> readFile(String fileName) {
        List<String> packetData = new ArrayList<>();
        File file = new File(fileName);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            assert sc != null;
            if (!sc.hasNext()) break;
            packetData.add(sc.nextLine());
        }
        return packetData;
    }

    public static void writeFile(List<Flow> flows, List<Packet> packets, List<Event> events, String fileName) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(fileName));

            pw.println("nFlows = " + flows.size());
            pw.println("nPackets = " + packets.size());
            for (Packet packet : packets) pw.println(packet);
            for (Event event : events) pw.println(event);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<String> packetData;
        List<Flow> flows = new ArrayList<>();
        List<Packet> packets = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        
        packetData = readFile("flows.txt");
        
        ArrayList<Boolean> checkFlows = new ArrayList<>();

        int numFlows = Integer.parseInt(packetData.get(0));

        for(int i = 0; i < numFlows; i++)
            flows.add(new Flow(i + 1, Double.parseDouble(packetData.get(i + 1))));

        int totalPackets = Integer.parseInt(packetData.get(numFlows + 1));

        for(int i = 0; i < totalPackets; i++) {
            int index = i + numFlows + 2;
            String[] tokenizedString = packetData.get(index).split(" ");

            double arrivalTime = Double.parseDouble(tokenizedString[0]);
            int flowId = Integer.parseInt(tokenizedString[1]) - 1;
            double len = Double.parseDouble(tokenizedString[2]);

            packets.add(new Packet(arrivalTime, flows.get(flowId), len));checkFlows.add(false);
        }

        double w = flows.get(0).getFlowWeight() - 0.1;

        double count = 1;
        int tCount = (int) (w / 0.1);
        int c = 1;

        double startTime = 0;
        double endTime = 2;

        int oCount = 0;
        
        events.add(new Event("pgpsDeparture", count, packets.get(0), startTime, endTime));
        checkFlows.set(0, true);
        startTime = startTime + 2;
        endTime = endTime + 2;
        count++;

        do {
            if (w > 0.1) {
                if (oCount < 2) {
                    events.add(new Event("pgpsDeparture", count, packets.get(tCount - c - 1), 0, 2 * (tCount + 1)));
                    int index = tCount - c - 1;
                    checkFlows.set(index, true);
                    c -= 1;
                    count++;
                } else if (oCount < 3) {
                    events.add(new Event("pgpsDeparture", count, packets.get(tCount - c - 4), 0, 2 * (tCount + 1)));
                    int index = tCount - c - 4;
                    checkFlows.set(index, true);
                    count++;
                } else {
                    for (int i = 0; i < packets.size(); i++)
                        if (packets.get(i).getPacketFlow().getFlowId() != 1 && !checkFlows.get(i)) {
                            events.add(new Event("pgpsDeparture", count, packets.get(i), 0, 2 * (tCount + 1)));
                            checkFlows.set(i, true);
                            count++;
                        }
                }

                for (int i = 0; i < packets.size(); i++) {
                    if (packets.get(i).getPacketFlow().getFlowId() == 1 && !checkFlows.get(i)) {
                        events.add(new Event("pgpsDeparture", count, packets.get(i), startTime, endTime));
                        checkFlows.set(i, true);
                        startTime = startTime + 2;
                        endTime = endTime + 2;
                        break;
                    }
                }
                w -= 0.1;
                count++;
                oCount++;
            } else {
                for (int i = 0; i < packets.size(); i++)
                    if (!checkFlows.get(i)) {
                        events.add(new Event("pgpsDeparture", count, packets.get(i), startTime, endTime));
                        checkFlows.set(i, true);
                        startTime = startTime + 2;
                        endTime = endTime + 2;
                        count++;
                    }
            }
        } while (checkFlows.contains(false));
        
        writeFile(flows, packets, events, "flowsout.txt");
    }
}
