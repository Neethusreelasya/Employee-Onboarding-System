package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Keyword-based onboarding assistant.
 * No database, no API key, no internet call: it matches the words in a question
 * against the entries below and returns the best answer.
 *
 * To change an answer: edit its text.
 * To make an answer easier to trigger: add more keywords (lowercase, no punctuation).
 * A keyword with a space at its start or end (for example " paid ") only matches a whole word.
 */
public class ChatBot {

    // Change this one line if the HR email changes.
    private static final String HR_EMAIL = "hr@company.com";

    private static final int MAX_QUESTION_LENGTH = 300;

    private static final String GREETING =
            "Hello! I'm the onboarding assistant. Ask me about working hours, leave, salary, "
            + "documents and more, or tap one of the questions below.";

    private static final String FALLBACK =
            "Sorry, I don't have information on that. Please contact HR at " + HR_EMAIL + ". "
            + "You can ask me about working hours, leave, salary, work from home, documents and more.";

    private static final Set<String> GREETINGS =
            Set.of("hi", "hii", "hello", "hey", "hola", "hi there", "hello there", "hey there");

    private static final Set<String> HELP_WORDS = Set.of("help", "menu", "options");

    private static class Entry {
        final String answer;
        final String[] keywords;

        Entry(String answer, String[] keywords) {
            this.answer = answer.strip().replace("{HR}", HR_EMAIL);
            this.keywords = keywords;
        }
    }

    // Entries are checked in this order. If two entries score the same, the earlier one wins.
    private static final List<Entry> ENTRIES = new ArrayList<>();

    private static void add(String answer, String... keywords) {
        ENTRIES.add(new Entry(answer, keywords));
    }

    private static final String HELP_ANSWER = """
            I can answer general questions about:
            • Working hours, holidays and dress code
            • Leave policy and how to apply for leave
            • Salary date, reimbursements, notice period and work from home
            • Your first day, documents and verification
            • Writing a leave reason or an HR reminder

            I can't see your personal tasks, documents or leave balance. Check your dashboard for those.
            """.strip();

    static {
        // ---------- Writing help ----------
        add("""
            Here are a few short reasons you can adapt for the Reason field:

            • Sick: "I am not feeling well and need rest, so I request leave for these dates."
            • Personal: "I have an urgent personal matter to attend to, so I request leave for these dates."
            • Family: "I need to attend a family function, so I request leave for these dates."
            • Planned: "I have planned time off and will complete my pending tasks before I leave."

            The form asks for a brief reason, so keep it to one sentence.
            """,
            "leave reason", "reason for leave", "reason for my leave", "reason for a leave",
            "write a leave", "draft a leave", "leave letter", "leave request letter", "leave email",
            "write a reason", "draft a reason", "sample reason", "reason for sick", "reason for casual",
            "write reason", "reason to apply");

        add("""
            Here is a reminder you can adapt:

            Subject: Reminder: Pending Onboarding Documents

            Dear Team,

            This is a friendly reminder to upload your pending onboarding documents in the Documents section of the portal by [date]. Each file can be up to 5 MB. If you face any problem, please reply to this email.

            Thank you,
            [Your name]
            HR Team
            """,
            "reminder", "remind", "write a reminder", "draft a reminder", "hr reminder",
            "send a reminder", "email to employees", "email to everyone",
            "message to everyone", "message to employees");

        // ---------- Leave ----------
        add("""
            To apply for leave:
            1. On your dashboard, open My Leaves.
            2. Click "+ Apply for Leave".
            3. Choose Sick, Casual or Earned Leave.
            4. Enter a brief reason, then the start and end dates.
            5. Click Submit Request.

            Your request appears in the History table with its status. Leave days are counted as calendar days from the start date to the end date, including weekends. Leave must be applied at least 1 day in advance, except sick leave.
            """,
            "apply for leave", "apply leave", "apply for a leave", "apply for my leave",
            "apply for sick", "apply for casual", "apply for earned", "apply for a sick",
            "request leave", "request a leave", "take leave", "take a leave",
            "leave application", "leave request", "leave form", "applying for leave",
            "apply my leave", "submit leave", "book leave", "ask for leave", "get leave",
            "get a leave", "need leave", "need a leave", "want leave");

        add("""
            Half-day leave isn't available in the portal. Leave is applied in whole days only. If you need a shorter absence, please contact HR at {HR}.
            """,
            "half day", "halfday", "half leave", "short leave", "hourly leave",
            "permission leave", "few hours leave", "leave for few hours", "half a day");

        add("""
            Open My Leaves from your dashboard. The Balance table shows how many days of each leave type you have used and how many are left, and the History table shows the status of every request. Requests are reviewed by your manager or HR. I can't see your personal leave data myself.
            """,
            "leave balance", "leaves left", "leaves remaining", "remaining leave", "remaining leaves",
            "leaves do i have", "leaves have i", "have left", "i have left", "leaves left in",
            "leaves are left", "used leave", "leaves used", "leaves i have taken",
            "balance", "leave history", "leave status", "status of my leave", "leave request status",
            "status of my request", "my leave request", "my leaves", "leave approved", "leave approval",
            "leave rejected", "leave was rejected", "request was rejected", "leave got rejected",
            "who approves", "approves leave", "approve my leave", "leave pending", "days left",
            "days remaining");

        add("""
            Leave policy:
            • Sick Leave: 6 days per year
            • Casual Leave: 12 days per year
            • Earned Leave: 15 days per year

            Leave must be applied at least 1 day in advance, except sick leave.
            """,
            "leave policy", "leave policies", "sick leave", "casual leave", "earned leave", "annual leave",
            "how many leaves", "how many days of leave", "how many leave", "leaves per year",
            "leaves in a year", "leave days", "types of leave", "leave types", "kinds of leave",
            "paid leave", "leave rules", "leave rule", "in advance", "advance notice",
            "leaves are there", "leaves do we get", "leaves do i get", "leave entitlement",
            "total leaves", "leave allowed", "leaves allowed", "leave per year",
            "rules for leave");

        add("""
            You can request leave during probation the same way as anyone else, from My Leaves on your dashboard, and your request is reviewed by your manager or HR. The length of the probation period isn't listed in the company information, so please confirm it with HR at {HR}.
            """,
            "probation", "probationary", "leave during probation", "after probation");

        // ---------- Work and pay ----------
        add("""
            Work from home isn't available during onboarding and probation. After probation it can be allowed with your manager's approval. For a specific case, contact HR at {HR}.
            """,
            "work from home", "wfh", "working from home", "work remotely", "remote work",
            " remote ", "hybrid", "from home", "work at home", "home office",
            "wfh during", "wfh in", "wfh after");

        add("""
            Salary is paid monthly, on the last working day of the month, into your bank account. For questions about your own salary amount or deductions, please contact HR at {HR}.
            """,
            "salary", " pay day", "payday", "when do i get paid", "when is salary", "get paid",
            "salary date", "pay date", " paid monthly", "payment date", "salary credit",
            "when will i be paid", "monthly pay", " paid ");

        add("""
            To claim a reimbursement, submit the bill with a short description to HR within 30 days of the expense. Your manager approves it, and it is paid with your next salary.
            """,
            "reimburse", "expense", " claim", "travel allowance", "bills", "travel bill", "petty cash");

        add("""
            The notice period is 30 days. To start the resignation process, please contact HR at {HR}.
            """,
            "notice period", "resign", " quit", " exit ", "exit process", "leaving the company",
            "leave the company", "leave the job");

        add("""
            If you are going to miss a task deadline, tell your manager or HR as early as possible and ask for more time. Repeated delays may be raised with your manager. You can see your tasks under My Tasks on your dashboard.
            """,
            "deadline", "overdue", "missed task", "miss a task", "miss the task", "late task",
            "task late", "running late", "extension", "cant finish", "cannot finish",
            "not able to finish", "unable to finish", "behind schedule", "delay in task",
            "delayed task", "after the deadline", "miss a deadline");

        // ---------- Documents ----------
        add("""
            Documents are normally verified within about 2 working days. If yours is taking longer, contact HR at {HR}. You can check the status of each document under Documents on your dashboard.
            """,
            "verification", "verified", "verify", "verification time", "document verification", "verify my");

        add("""
            To upload a document, open Documents on your dashboard and upload the file for the document HR has requested. Each file can be up to 5 MB, and PDF or clear image scans (JPG or PNG) work best.
            """,
            "file format", "file type", "file size", "size limit", "upload", "format", "pdf", "jpg",
            "jpeg", "png", "allowed file", "max size", "maximum size", "how big",
            "submit document", "submit my document", "submit a document", "submit the document");

        // ---------- First day and contacts (before "documents required" so ties go here) ----------
        add("""
            On your first day:
            • Report to the HR desk at 9:30 AM, when working hours start.
            • Meet your reporting manager.
            • Complete the tasks and documents assigned to you on your dashboard.

            Bring original ID proof, address proof, educational certificates, passport-size photos and bank account details. The dress code is business casual from Monday to Thursday, and casual wear is permitted on Fridays.
            """,
            "first day", "day one", "day 1", "joining day", "day of joining", "what to bring",
            "should i bring", "need to bring", "bring on", "bring with", "first week", "get started",
            "how do i start", "where do i start", "what should i do first", "what to do first",
            "report to", "where to report", "where do i report", "joining formalities",
            "on joining", "after joining");

        add("""
            The documents HR has asked for are listed under Documents on your dashboard, each with its current status, so you can see what is still pending there.
            """,
            "which documents", "what documents", "documents needed", "documents required",
            "required documents", "documents do i need", "need to upload", "still need to submit",
            "documents to submit", "documents to upload", "documents i need", "list of documents",
            "mandatory documents");

        add("""
            I can't see your personal information (tasks, documents, progress or manager), so I can only answer general questions. You can see your own details from your dashboard: My Tasks, Documents, Progress and My Leaves. For anything else, contact HR at {HR}.
            """,
            "my tasks", "pending task", "tasks pending", "tasks are pending", "what tasks",
            "task status", "tasks do i have", "tasks due", "due this week", "what is due",
            "due today", "my progress", "onboarding progress", "progress", "my documents",
            "document status", "documents pending", "pending documents", "rejected", "not approved",
            "who is my manager", "my manager", "reporting manager", "my hr", "my profile",
            "my details", "my joining", "joining date");

        add("""
            Laptop, ID card and email access are handled through HR. Please contact HR at {HR} if you haven't received something yet.
            """,
            "laptop", "id card", "email access", "system access", "equipment", "company email",
            "official email", "work email", "email account", "access card", "asset", "it support",
            "it team", "computer", "workstation", "login credentials");

        // ---------- Company information ----------
        add("""
            Working hours:
            • Monday to Friday: 9:30 AM - 6:30 PM
            • Saturday: 10:00 AM - 2:00 PM (alternate Saturdays)
            • Sunday: Holiday
            """,
            "working hours", "work hours", "office hours", "office timing", "work timing", "timing",
            "office time", "when does office", "office start", "office open", "office close",
            "start time", "end time", " shift", "saturday", "sunday", "weekend",
            "alternate saturday", "working days", "work days", "how many hours");

        add("""
            Sunday is a weekly holiday, and Saturdays are half days (10:00 AM - 2:00 PM) on alternate weeks. A list of public holidays isn't available in the portal, so please check with HR at {HR}.
            """,
            "holiday", "festival", "public holiday", "off day", "day off", "days off", "vacation");

        add("""
            The dress code is business casual from Monday to Thursday. Casual wear is permitted on Fridays.
            """,
            "dress code", "what to wear", "attire", "clothes", "clothing", "jeans", "tshirt", "t shirt",
            "formal wear", "casual wear", "business casual", "friday casual", " wear", "can i wear");

        add("""
            Code of conduct:
            • Maintain professionalism and respect toward colleagues.
            • Confidentiality of company and client data is mandatory.
            • Report any workplace concerns to HR promptly at {HR}.
            """,
            "code of conduct", "conduct", "confidential", "workplace concern", "workplace issue",
            "report a concern", "report an issue", "report a problem", "complaint", "harassment",
            "harass", "ethics", "misbehav", "discrimination");

        add("""
            You can reach HR by email at {HR}.
            """,
            "hr email", "email of hr", "hr email id", "hr contact", "contact hr", "hr phone",
            "hr number", "phone number", "hr mail", "email hr", "reach hr", "talk to hr",
            "speak to hr", "meet hr", "who is hr", "hr team", "hr department", "human resources",
            "contact details", "mail id", "hr id", "how to contact", "contact number");

        add("""
            We are a technology-driven organization committed to innovation, integrity and employee growth. This onboarding portal helps every new team member settle in smoothly and understand our workplace expectations. The Company Information page has the full details.
            """,
            "about us", "about the company", "about company", "company information", "company info",
            "company details", "company profile", "about your company", "company policies",
            "company policy", "policies", "what does the company", "tell me about", "who are we",
            "organization", "organisation");

        // ---------- Small talk ----------
        add("""
            You're welcome! Ask me anything else about working hours, leave, salary and more.
            """,
            "thank", "thx", "appreciate");

        add("""
            I'm the onboarding assistant, a simple built-in helper that answers common questions from the company information. I'm not a person, and I can't see your personal data.

            """ + HELP_ANSWER,
            "what can you do", "what can i ask", "what do you know", "how can you help",
            "your features", "who are you", "what are you", "are you a bot", "are you ai",
            "are you human", "are you a robot", "what is your name", "your name", "how do you work");
    }

    /** Returns the assistant's answer to a question. Never returns null. */
    public static String reply(String question) {
        if (question == null || question.isBlank()) {
            return "Please type a question.";
        }

        String cut = question.length() > MAX_QUESTION_LENGTH
                ? question.substring(0, MAX_QUESTION_LENGTH)
                : question;
        String text = normalize(cut);

        if (text.isEmpty()) {
            return "Please type a question.";
        }
        if (GREETINGS.contains(text)) {
            return GREETING;
        }
        if (HELP_WORDS.contains(text)) {
            return HELP_ANSWER;
        }

        String padded = " " + text + " ";
        Entry best = null;
        int bestScore = 0;

        for (Entry entry : ENTRIES) {
            int score = 0;
            for (String keyword : entry.keywords) {
                if (padded.contains(keyword)) {
                    score += keyword.strip().length();
                }
            }
            if (score > bestScore) {
                bestScore = score;
                best = entry;
            }
        }

        return best == null ? FALLBACK : best.answer;
    }

    private static String normalize(String text) {
        String t = text.toLowerCase(Locale.ROOT);
        t = t.replace("'", "").replace("\u2019", "");
        return t.replaceAll("[^a-z0-9]+", " ").trim();
    }
}
