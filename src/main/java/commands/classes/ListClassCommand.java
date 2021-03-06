package commands.classes;

import commands.Command;
import constants.Blanks;
import dao.ClassDAO;
import dao.SectionDAO;
import entities.Class;
import entities.Section;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListClassCommand implements Command {
    private SectionDAO sectionDAO;
    private ClassDAO classDAO;

    public ListClassCommand(SectionDAO sectionDAO, ClassDAO classDAO) {
        this.sectionDAO = sectionDAO;
        this.classDAO = classDAO;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int currentPage = Integer.valueOf(
                request.getParameter("currentPage") == null
                        ? "1"
                        : request.getParameter("currentPage")
        );
        int recordsPerPage = Integer.valueOf(
                request.getParameter("recordsPerPage") == null
                        ? "5"
                        : request.getParameter("recordsPerPage")
        );

        int id = Integer.parseInt(request.getParameter("id"));
        int noOfRecords = classDAO.countClassesById(id);

        int numberOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        List<Class> classes = classDAO.listClassesById((currentPage - 1) * recordsPerPage, recordsPerPage, id);
        request.setAttribute("classes", classes);
        request.setAttribute("id", id);
        request.setAttribute("noOfPages", numberOfPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("number", noOfRecords);
        RequestDispatcher dispatcher = request.getRequestDispatcher(Blanks.LIST_CLASS_PAGE);
        dispatcher.forward(request, response);
    }
}
