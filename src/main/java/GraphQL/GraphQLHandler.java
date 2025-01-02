package GraphQL;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Map;

public class GraphQLHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    private static final GraphQL graphQL = createGraphQL();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // Log der Anfrage
        System.out.println("HTTP Request erhalten:");
        System.out.println("Methode: " + request.method());
        System.out.println("URI: " + request.uri());
        System.out.println("Header: " + request.headers());
        System.out.println("Body: " + request.content().toString(StandardCharsets.UTF_8));

        // Verarbeite nur POST-Anfragen
        if (request.method() != HttpMethod.POST) {
            sendResponse(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "Method not allowed");
            return;
        }

        try {
            // JSON-Body auslesen und parsen
            String requestBody = request.content().toString(StandardCharsets.UTF_8);
            Map<String, Object> requestMap = objectMapper.readValue(requestBody, Map.class);
            String query = (String) requestMap.get("query");

            // GraphQL-Anfrage ausführen
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .build();

            Map<String, Object> result = graphQL.execute(executionInput).toSpecification();
            String jsonResponse = objectMapper.writeValueAsString(result);

            // Log der Antwort
            System.out.println("GraphQL Response: " + jsonResponse);

            // Antwort senden
            sendResponse(ctx, HttpResponseStatus.OK, jsonResponse);
        } catch (Exception e) {
            // Fehler loggen
            System.err.println("Fehler bei der Verarbeitung der Anfrage: " + e.getMessage());
            e.printStackTrace();
            sendResponse(ctx, HttpResponseStatus.BAD_REQUEST, "Invalid request");
        }
    }

    private static GraphQL createGraphQL() {
        InputStream schemaStream = GraphQLHandler.class.getResourceAsStream("/schema.graphqls");
        if (schemaStream == null) {
            throw new RuntimeException("Schema-Datei 'schema.graphqls' wurde nicht gefunden.");
        }

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaStream);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("getAllUsers", env -> new UserResolver().getAllUsers())
                        .dataFetcher("getUserById", env -> new UserResolver().getUserById(env.getArgument("id")))
                        .dataFetcher("getUserByName", env -> new UserResolver().getUserByName(env.getArgument("username")))
                        .dataFetcher("getAllUebungen", env -> new MuskelTrainingResolver().getAllUebungen()))
                .type("Mutation", builder -> builder
                        .dataFetcher("createUser", env -> new UserResolver().createUser(env.getArgument("username"), env.getArgument("password")))
                        .dataFetcher("updateUser", env -> new UserResolver().updateUser(env.getArgument("id"), env.getArgument("username"), env.getArgument("password")))
                        .dataFetcher("deleteUser", env -> new UserResolver().deleteUser(env.getArgument("id")))
                        .dataFetcher("createTrainingMitMuskeln", env -> new MuskelTrainingResolver().createTrainingMitMuskeln(
                                env.getArgument("trainingName"),
                                env.getArgument("description"),
                                env.getArgument("muskelNamen")))
                        .dataFetcher("addMuskelToTraining", env -> new MuskelTrainingResolver().addMuskelToTraining(
                                env.getArgument("trainingName"),
                                env.getArgument("muskelName")))
                        .dataFetcher("deleteTraining", env -> new MuskelTrainingResolver().deleteTraining(env.getArgument("trainingName"))))
                .build();

        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        return GraphQL.newGraphQL(schema).build();
    }

    private void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String content) {
        System.out.println("HTTP Response (vor dem Senden):");
        System.out.println("Status: " + status);
        System.out.println("Body: " + content);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(content, StandardCharsets.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.length());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
