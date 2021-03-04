import core.Line;
import core.Station;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteCalculatorTest {

    StationIndex stationIndex;
    RouteCalculator routeCalculator;

    @Before
    public void setUp() throws Exception {

        stationIndex = new StationIndex();

        stationIndex.addLine(new Line(1, "Именная"));
        stationIndex.addLine(new Line(2, "Компасная"));
        stationIndex.addLine(new Line(3, "Фруктовая"));


        stationIndex.addStation(new Station("Петровская", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Ивановская", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Васильевская", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Сидоровская", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Николаевская", stationIndex.getLine(1)));
        stationIndex.addStation(new Station("Южная", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Северная", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Восточная", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Западная", stationIndex.getLine(2)));
        stationIndex.addStation(new Station("Яблочная", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("Апельсиновая", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("Грушёвая", stationIndex.getLine(3)));
        stationIndex.addStation(new Station("Персиковая", stationIndex.getLine(3)));

        stationIndex.getLine(1).addStation(stationIndex.getStation("Петровская"));
        stationIndex.getLine(1).addStation(stationIndex.getStation("Ивановская"));
        stationIndex.getLine(1).addStation(stationIndex.getStation("Васильевская"));
        stationIndex.getLine(1).addStation(stationIndex.getStation("Сидоровская"));
        stationIndex.getLine(1).addStation(stationIndex.getStation("Николаевская"));

        stationIndex.getLine(2).addStation(stationIndex.getStation("Южная"));
        stationIndex.getLine(2).addStation(stationIndex.getStation("Северная"));
        stationIndex.getLine(2).addStation(stationIndex.getStation("Восточная"));
        stationIndex.getLine(2).addStation(stationIndex.getStation("Западная"));

        stationIndex.getLine(3).addStation(stationIndex.getStation("Яблочная"));
        stationIndex.getLine(3).addStation(stationIndex.getStation("Апельсиновая"));
        stationIndex.getLine(3).addStation(stationIndex.getStation("Грушёвая"));
        stationIndex.getLine(3).addStation(stationIndex.getStation("Персиковая"));

        List<Station> connectionStations1 = new ArrayList<>();
        connectionStations1.add(stationIndex.getStation("Васильевская", 1));
        connectionStations1.add(stationIndex.getStation("Северная", 2));
        stationIndex.addConnection(connectionStations1);

        List<Station> connectionStations2 = new ArrayList<>();
        connectionStations2.add(stationIndex.getStation("Апельсиновая", 3));
        connectionStations2.add(stationIndex.getStation("Восточная", 2));
        stationIndex.addConnection(connectionStations2);

        routeCalculator = new RouteCalculator(stationIndex);
    }

    @Test
    public void testGetShortestRoute(){

        Station from = stationIndex.getStation("Петровская");
        Station to = stationIndex.getStation("Сидоровская");
        List<Station> route = routeCalculator.getShortestRoute(from, to);

        List<Station> expectedRoute = new ArrayList<>();
        expectedRoute.add(stationIndex.getStation("Петровская"));
        expectedRoute.add(stationIndex.getStation("Ивановская"));
        expectedRoute.add(stationIndex.getStation("Васильевская"));
        expectedRoute.add(stationIndex.getStation("Сидоровская"));

        assertEquals(expectedRoute, route);
    }

    @Test
    public void testGetShortestRoute1(){

        Station from = stationIndex.getStation("Ивановская");
        Station to = stationIndex.getStation("Западная");
        List<Station> route = routeCalculator.getShortestRoute(from, to);

        List<Station> expectedRoute = new ArrayList<>();
        expectedRoute.add(stationIndex.getStation("Ивановская"));
        expectedRoute.add(stationIndex.getStation("Васильевская"));
        expectedRoute.add(stationIndex.getStation("Северная"));
        expectedRoute.add(stationIndex.getStation("Восточная"));
        expectedRoute.add(stationIndex.getStation("Западная"));

        assertEquals(expectedRoute, route);
    }

    @Test
    public void testGetShortestRoute2(){

        Station from = stationIndex.getStation("Персиковая");
        Station to = stationIndex.getStation("Николаевская");
        List<Station> route = routeCalculator.getShortestRoute(from, to);

        List<Station> expectedRoute = new ArrayList<>();
        expectedRoute.add(stationIndex.getStation("Персиковая"));
        expectedRoute.add(stationIndex.getStation("Грушёвая"));
        expectedRoute.add(stationIndex.getStation("Апельсиновая"));
        expectedRoute.add(stationIndex.getStation("Восточная"));
        expectedRoute.add(stationIndex.getStation("Северная"));
        expectedRoute.add(stationIndex.getStation("Васильевская"));
        expectedRoute.add(stationIndex.getStation("Сидоровская"));
        expectedRoute.add(stationIndex.getStation("Николаевская"));

        assertEquals(expectedRoute, route);
    }

    @Test
    public void testCalculateDuration() {
        Station from = stationIndex.getStation("Персиковая");
        Station to = stationIndex.getStation("Николаевская");
        List<Station> route = routeCalculator.getShortestRoute(from, to);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 19.5;
        assertEquals(actual, expected, 0);
    }
}
