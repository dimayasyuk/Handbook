package commands.classes;

import commands.section.ModifierService;
import constants.Blanks;
import constants.URL;
import dao.SectionDAO;
import entities.Section;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import servlets.ClassServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

public class NewClassCommandTest {
    private ServletContext servletContext;
    private HttpServletResponse mockResponse;
    private HttpServletRequest mockRequest;
    private SectionDAO sectionDAO;
    private List<Section> neededSections;
    private List<Section> initialSections;

    @Before
    public void init() throws IOException, SQLException {
        servletContext = Mockito.mock(ServletContext.class);
        mockRequest = Mockito.mock(HttpServletRequest.class);
        sectionDAO = Mockito.mock(SectionDAO.class);
        Mockito.doAnswer((args) -> {
            if (args.getArgument(1) instanceof List<?>) {
                neededSections = args.getArgument(1);
            }
            return null;
        }).when(mockRequest).setAttribute(Mockito.any(), Mockito.any());
        mockResponse = Mockito.mock(HttpServletResponse.class);
        initialSections = ModifierService.initSections();
    }

    @Test
    public void execute() throws ServletException, IOException, SQLException {
        ClassServlet classServlet = new ClassServlet() {
            public ServletContext getServletContext() {
                return servletContext;
            }
        };
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn(Blanks.BASE_URL + URL.NEW_CLASS);

        Mockito.doReturn("jdbc:mysql://localhost:3306/vocabulary?serverTimezone=UTC").when(servletContext).getInitParameter("jdbcURL");
        Mockito.doReturn("root").when(servletContext).getInitParameter("jdbcUsername");
        Mockito.doReturn("12345").when(servletContext).getInitParameter("jdbcPassword");

        Mockito.doReturn(initialSections).when(sectionDAO).listSections();
        Mockito.when(mockRequest.getRequestDispatcher(eq(Blanks.NEW_CLASS_PAGE))).thenReturn(requestDispatcher);

        classServlet.init();
        classServlet.setModifierDAO(sectionDAO);
        classServlet.doGet(mockRequest, mockResponse);

        assertEquals(initialSections, neededSections);
    }
}