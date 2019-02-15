import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ClientActions extends Thread {
    private Socket client;
    private int clientID;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private double latitude, longitude;
    private ArrayList<POI> pois, best = new ArrayList<>();
    private int users, POIs;
    private RealMatrix X, Y;

    ClientActions(Socket connection, int idCounter) {
        this.client = connection;
        this.clientID = idCounter;
    }

    void initialize(final int users, final int POIs, final RealMatrix X, final RealMatrix Y, ArrayList<POI> POI_List) {
        this.users = users;
        this.POIs = POIs;
        this.X = X.copy();
        this.Y = Y.copy();
        this.pois = POI_List;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            out.writeInt(users);
            out.flush();
            int user = in.readInt();
            int reply = (user > users ? -1 : 0);

            if (reply == 0) {
                best = calculateBestLocalPoisForUsers(user);

                String category = in.readUTF();
                int k_top = in.readInt();
                Collections.sort(best, (o1, o2) -> Double.valueOf(o2.getFrequency()).compareTo(o1.getFrequency()));
                int count = (int) (best.stream()
                        .filter(poi -> poi.getPOI_category_id().equalsIgnoreCase(category)))
                        .count();

                if (count < k_top)
                    k_top = count;

                out.writeInt(k_top);
                out.flush();

                List<POI> newList = best.stream()
                        .filter(poi -> poi.getPOI_category_id().equalsIgnoreCase(category))
                        .limit(k_top)
                        .collect(Collectors.toList());

                for (POI p : newList) {
                    out.writeInt(p.getID());
                    out.flush();
                    out.writeUTF(p.getPOI_name());
                    out.flush();
                    out.writeDouble(p.getLatitude());
                    out.flush();
                    out.writeDouble(p.getLongitude());
                    out.flush();
                    out.writeUTF(p.getPhotos());
                    out.flush();
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private ArrayList<POI> calculateBestLocalPoisForUsers(int user) {
        ArrayList<POI> poi = new ArrayList<>();
        ArrayList<Double> arr = new ArrayList<>();
        int[] position = new int[POIs];

        //RealMatrix rui = calculate_x_u(u, YY).transpose().multiply(calculate_y_i(i, XX));
        for (int i = 0; i < POIs; i++) {
            RealMatrix rui = X.getRowMatrix(user).multiply(Y.getRowMatrix(i).transpose());
            arr.add(rui.getEntry(0, 0));
            position[i] = i;
            int j = 0;
            while (pois.get(j).getID() != i) {
                j++;
            }
            pois.get(j).setFrequency(arr.get(i));
            poi.add(pois.get(j));
        }

        return poi;
    }
}
