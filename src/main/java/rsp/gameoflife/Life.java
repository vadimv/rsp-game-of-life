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
        final Component<State> component = useState -> {
                final var cells = useState.get().board.cells;
                return html(head(link(attr("rel", "stylesheet"), attr("href", "/res/style.css"))),
                        body(div(attr("class", "tetris-wrapper"),
                                div(attr("class", "board"),
                                        of(IntStream.range(0, cells.length)
                                                    .mapToObj(index ->
                                                        div(attr("class", "cell t" + (cells[index] ? "J" : "0")),
                                                                on("click", c -> {
                                                                    System.out.println("Clicked x=" + Board.x(index) + " y=" + Board.y(index));
                                                                    useState.accept(useState.get().toggleCell(Board.x(index), Board.y(index)));
                                                                })))))),
                                div(attr("class", "controls"),
                                        button(attr("id", "start-btn"), attr("type", "button"),
                                                when(false, () -> attr("disabled")),
                                                text("Start"), on("click", c -> {
                                                    System.out.println("Start");
                                                    c.scheduleAtFixedRate(() -> useState.accept(s -> s.advance()),
                                                            0, 200, TimeUnit.MILLISECONDS);
                                                })))));
        };
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
}
