package rsp.gameoflife;

import rsp.App;
import rsp.Component;
import rsp.jetty.JettyServer;
import rsp.server.StaticResources;
import rsp.util.data.Tuple2;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static rsp.dsl.Html.*;

/**
 * An implementation of Conway's Game of Life.
 */
public class Life {

    public static void main(String[] args) throws Exception {
        final Component<State> component = useState ->
                html(head(link(attr("rel", "stylesheet"), attr("href","/res/style.css"))),
                     body(div(attr("class", "tetris-wrapper"),
                             div(attr("class", "board"),
                                     of(cellsStreamWithIndices(useState.get().board.cells)
                                             .map(cell ->
                                                     div(attr("class", "cell t" + (cell._1 ? "J" : "0")),
                                                         on("click", c -> {
                                                            System.out.println("Clicked x=" + Board.x(cell._2) + " y=" + Board.y(cell._2));
                                                            useState.accept(useState.get().toggleCell(Board.x(cell._2), Board.y(cell._2)));
                                                     })))))),
                             div(attr("class", "controls"),
                                     button(attr("id", "start-btn"), attr("type", "button"),
                                             when(false, () -> attr("disabled")),
                                             text("Start"), on("click", c -> {
                                                     System.out.println("Start");
                                                     c.scheduleAtFixedRate(() -> useState.accept(s -> s.advance()),
                                                                      0, 200, TimeUnit.MILLISECONDS);
                                             })))));

        final var initialState = State.initialState();
        final var s = new JettyServer(8080,
                                      "",
                                       new App(initialState,
                                               component),
                                       new StaticResources(new File("src/main/java/rsp/gameoflife"),
                                                          "/res/*"));
        s.start();
        s.join();
    }

    private static Stream<Tuple2<Boolean, Integer>> cellsStreamWithIndices(boolean[] cells) {
        return IntStream.range(0, cells.length).mapToObj(idx -> new Tuple2<>(cells[idx], idx));
    }
}
