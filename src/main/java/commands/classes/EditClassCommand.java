package commands.classes;

import commands.Command;
import constants.Blanks;
import dao.ClassDAO;
import dao.ContentDAO;
import dao.SectionDAO;
import entities.Class;
import entities.Content;
import entities.Section;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EditClassCommand implements Command {
    private SectionDAO sectionDAO;
    private ClassDAO classDAO;
    private ContentDAO contentDAO;

    public EditClassCommand(SectionDAO sectionDAO, ClassDAO classDAO, ContentDAO contentDAO) {
        this.sectionDAO = sectionDAO;
        this.classDAO = classDAO;
        this.contentDAO = contentDAO;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Class existingClass = classDAO.getClassById(id);
        List<Section> listSection = sectionDAO.listSections();
        Content content = contentDAO.getContentById(existingClass.getContentId());
        RequestDispatcher dispatcher = request.getRequestDispatcher(Blanks.NEW_CLASS_PAGE);
        request.setAttribute("cls", existingClass);
        request.setAttribute("cont", content);
        request.setAttribute("sections", listSection);

        dispatcher.forward(request, response);
    }
}
