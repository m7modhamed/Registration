package com.stickynotes.notes;

import com.stickynotes.User.IUserService;
import com.stickynotes.User.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

    private final InoteService noteService;
    private final IUserService userService;

    @GetMapping("/")
    public String viewMyNotes(Model model,HttpServletRequest http) {
        Optional<User> user= getCurrentUser(http);
        List<Note> notes=null;
        if (user.isPresent()) {
             notes=user.get().getNotes();
        }
        model.addAttribute("notes",notes) ;
        return "myNotes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id,Model model){
        Optional<Note> note= noteService.FindNote(id);
        if(note.isPresent()) {
            model.addAttribute("note", note.get());
            return "add-note-form";
        }else{
            return "redirect:/notes/";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable("id") int id,Model model){
        noteService.deleteNote(id);
            return "redirect:/notes/";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        Note note=new Note();
        note.setColor("#ffffff");
        model.addAttribute("note",note);
        return "add-note-form";
    }

    @PostMapping("/addnote")
    public String addNote(@ModelAttribute("note") Note note, HttpServletRequest http) {
        Optional<User> user= getCurrentUser(http);

        if (user.isPresent()) {
            user.get().getNotes().add(note);
        }
        note.setState("to_do");
        noteService.saveNote(note);
        return "redirect:/notes/";
    }

    @GetMapping("/move/{pos}/{id}")
    public String moveNote(@PathVariable int pos,@PathVariable("id") Note note){
        if(pos == 1){
            note.setState("to_do");

        } else if (pos == 2) {
            note.setState("progress");
        } else if (pos == 3) {
            note.setState("done");
        }
        noteService.saveNote(note);
        return "redirect:/notes/";
    }


    private Optional<User> getCurrentUser(HttpServletRequest http){
        Principal principal = http.getUserPrincipal();
        String email = principal.getName();
        Optional<User> user = userService.findUserByEmail(email);
        return user;
    }
}
