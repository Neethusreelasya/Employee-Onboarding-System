<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>
    <link rel="stylesheet" href="style.css">

    <!-- Onboarding assistant styles. Self-contained: does not depend on style.css -->
    <style>
        .oa-panel, .oa-panel *, .oa-launcher { box-sizing: border-box; }

        .oa-face { display: block; width: 100%; height: 100%; }

        .oa-avatar {
            flex: none; width: 24px; height: 24px; padding: 2px; border-radius: 50%;
            background: linear-gradient(135deg, #8e2de2, #4b0082);
            box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.7);
        }
        .oa-avatar-sm { width: 22px; height: 22px; box-shadow: none; }

        .oa-launcher {
            position: fixed; right: 18px; bottom: 18px; z-index: 1000;
            width: 48px; height: 48px; padding: 5px; border-radius: 50%;
            border: 2px solid #ff6600;
            background: linear-gradient(135deg, #8e2de2, #4b0082);
            box-shadow: 0 4px 14px rgba(75, 0, 130, 0.35);
        }

        .oa-panel {
            position: fixed; right: 18px; bottom: 78px; z-index: 1000;
            width: 300px; max-width: calc(100vw - 24px);
            height: 380px; max-height: calc(100vh - 100px);
            display: none; flex-direction: column;
            background: #fff; border-radius: 10px;
            box-shadow: 0 8px 28px rgba(75, 0, 130, 0.28);
            overflow: hidden;
        }
        .oa-panel.open { display: flex; }

        @media (prefers-reduced-motion: no-preference) {
            .oa-panel.open { animation: oa-open 0.18s ease-out; }
        }
        @keyframes oa-open {
            from { opacity: 0; transform: translateY(8px); }
            to   { opacity: 1; transform: none; }
        }

        .oa-head {
            flex: none; display: flex; justify-content: space-between; align-items: center;
            gap: 10px; padding: 8px 10px; color: #fff;
            background: linear-gradient(135deg, #8e2de2, #4b0082);
            border-bottom: 2px solid #ff6600;
        }
        .oa-brand { display: flex; align-items: center; gap: 8px; min-width: 0; }
        .oa-title { font-size: 14px; font-weight: 600; line-height: 1.2; }
        .oa-min {
            background: transparent; color: #fff; height: 26px; padding: 0 9px;
            font-size: 22px; line-height: 1; border-radius: 6px;
        }
        .oa-min:hover { background: rgba(255, 255, 255, 0.18); transform: none; box-shadow: none; }

        .oa-note {
            flex: none; margin: 8px 10px 0; padding: 6px 10px;
            background: #f9f4ff; border: 1px solid #e6dcf3; border-radius: 8px;
            color: #4b0082; font-size: 11px; line-height: 1.4;
        }

        .oa-log {
            flex: 1; min-height: 0; overflow-y: auto; padding: 10px;
            display: flex; flex-direction: column; gap: 8px; background: #f4f6fa;
        }
        .oa-row { display: flex; align-items: flex-end; gap: 6px; }
        .oa-row-user { justify-content: flex-end; }
        .oa-msg {
            max-width: 84%; padding: 7px 10px; border-radius: 10px;
            font-size: 13px; line-height: 1.45; white-space: pre-wrap; overflow-wrap: anywhere;
        }
        .oa-bot { background: #fff; color: #2d2d2d; border: 1px solid #e6dcf3; border-bottom-left-radius: 3px; }
        .oa-user { background: linear-gradient(135deg, #8e2de2, #6a0dad); color: #fff; border-bottom-right-radius: 3px; }
        .oa-pending { color: #777; font-style: italic; }

        .oa-chips {
            flex: none; display: flex; flex-wrap: wrap; gap: 5px; padding: 8px 10px;
            background: #fff; border-top: 1px solid #eee; max-height: 78px; overflow-y: auto;
        }
        .oa-chip {
            background: #fff; color: #6a0dad; border: 1px solid #8e2de2;
            padding: 3px 9px; border-radius: 12px; font-size: 11px; font-weight: 500;
        }
        .oa-chip:hover { background: #f9f4ff; transform: none; box-shadow: none; }

        .oa-input-row {
            flex: none; display: flex; align-items: center; gap: 6px;
            padding: 8px 10px; background: #fff; border-top: 1px solid #eee;
        }
        .oa-input-row input[type="text"] {
            flex: 1; min-width: 0; width: auto; margin-top: 0; padding: 8px 10px; font-size: 13px;
        }
        .oa-icon-btn {
            flex: none; width: 34px; height: 34px; padding: 0; border-radius: 8px;
            display: flex; align-items: center; justify-content: center;
        }
        .oa-icon-btn svg {
            width: 18px; height: 18px; fill: none; stroke: currentColor;
            stroke-width: 2; stroke-linecap: round; stroke-linejoin: round;
        }
        .oa-new { background: linear-gradient(135deg, #6a0dad, #4b0082); }
        .oa-icon-btn:disabled { opacity: 0.55; cursor: default; transform: none; box-shadow: none; }

        .oa-launcher:focus-visible, .oa-min:focus-visible,
        .oa-chip:focus-visible, .oa-icon-btn:focus-visible {
            outline: 2px solid #ff6600; outline-offset: 2px;
        }

        @media (max-width: 480px) {
            .oa-panel { left: 8px; right: 8px; bottom: 72px; width: auto; max-width: none; }
            .oa-launcher { right: 12px; bottom: 12px; }
        }
    </style>
</head>
<body>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }
    String name = (String) userSession.getAttribute("name");
%>
    <div class="welcome-box">
        <h2>Welcome, <%= name %></h2>
    </div>
    <ul>
        <li><a href="MyTasksServlet">My Tasks</a></li>
        <li><a href="MyDocumentsServlet">Documents</a></li>
        <li><a href="MyProgressServlet">Progress</a></li>
        <li><a href="MyLeavesServlet">My Leaves</a></li>
    <li><a href="MyResignationServlet">My Resignation</a></li>
    <li><a href="MyProfileServlet">My Profile</a></li>
</ul>
    <a href="LogoutServlet">Logout</a>

    <!-- ===== Onboarding assistant (fixed at the bottom-right, does not move the page content) ===== -->

    <!-- Robot face, defined once and reused below -->
    <svg width="0" height="0" style="position:absolute" aria-hidden="true" focusable="false">
        <symbol id="botFace" viewBox="0 0 64 64">
            <line x1="32" y1="9" x2="32" y2="16" stroke="#f4f1fa" stroke-width="2.5" stroke-linecap="round"/>
            <circle cx="32" cy="8" r="3.2" fill="#ff6600"/>
            <rect x="6" y="28" width="7" height="14" rx="3.5" fill="#d9cdea"/>
            <rect x="51" y="28" width="7" height="14" rx="3.5" fill="#d9cdea"/>
            <rect x="11" y="16" width="42" height="38" rx="15" fill="#f4f1fa"/>
            <rect x="17" y="26" width="30" height="18" rx="9" fill="#24093f"/>
            <circle cx="25.5" cy="35" r="3.6" fill="#5ef0ff"/>
            <circle cx="38.5" cy="35" r="3.6" fill="#5ef0ff"/>
            <circle cx="26.6" cy="33.9" r="1.1" fill="#ffffff"/>
            <circle cx="39.6" cy="33.9" r="1.1" fill="#ffffff"/>
        </symbol>
    </svg>

    <div class="oa-panel" id="chatPanel" role="dialog" aria-label="Onboarding assistant">
        <div class="oa-head">
            <div class="oa-brand">
                <span class="oa-avatar"><svg class="oa-face" width="20" height="20" aria-hidden="true"><use href="#botFace"></use></svg></span>
                <span class="oa-title">Onboarding assistant</span>
            </div>
            <button type="button" class="oa-min" id="chatMin" aria-label="Minimize assistant">&minus;</button>
        </div>

        <div class="oa-note">Answers come from company information. Please don't enter personal details.</div>

        <div class="oa-log" id="chatLog" aria-live="polite"></div>

        <div class="oa-chips" id="chatChips">
            <button type="button" class="oa-chip" data-q="What are the working hours?">Working hours</button>
            <button type="button" class="oa-chip" data-q="How do I apply for leave?">Apply for leave</button>
            <button type="button" class="oa-chip" data-q="How many leaves do I get?">Leave policy</button>
            <button type="button" class="oa-chip" data-q="What should I bring on my first day?">First day</button>
            <button type="button" class="oa-chip" data-q="When is salary paid?">Salary date</button>
            <button type="button" class="oa-chip" data-q="Help me write a leave reason">Leave reason</button>
        </div>

        <div class="oa-input-row">
            <input type="text" id="chatInput" maxlength="300" placeholder="Ask the assistant"
                   aria-label="Your question" autocomplete="off">
            <button type="button" class="oa-icon-btn" id="chatSend" aria-label="Send question">
                <svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M22 2 11 13"/><path d="M22 2 15 22l-4-9-9-4z"/></svg>
            </button>
            <button type="button" class="oa-icon-btn oa-new" id="chatNew" aria-label="Start a new conversation" title="New conversation">
                <svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M21 12a9 9 0 1 1-3-6.7"/><path d="M21 3v6h-6"/></svg>
            </button>
        </div>
    </div>

    <button type="button" class="oa-launcher" id="chatLauncher"
            aria-expanded="false" aria-controls="chatPanel" aria-label="Open onboarding assistant">
        <svg class="oa-face" width="34" height="34" aria-hidden="true"><use href="#botFace"></use></svg>
    </button>

    <script>
    (function () {
        var GREETING = "Hello! I'm the onboarding assistant. Ask me about working hours, leave, salary, documents and more, or tap one of the questions below.";
        // false = only the small robot button shows until the employee clicks it.
        // true  = the chat box starts open on wide screens.
        var OPEN_BY_DEFAULT = false;

        var launcher = document.getElementById('chatLauncher');
        var panel = document.getElementById('chatPanel');
        var minBtn = document.getElementById('chatMin');
        var log = document.getElementById('chatLog');
        var chips = document.getElementById('chatChips');
        var input = document.getElementById('chatInput');
        var sendBtn = document.getElementById('chatSend');
        var newBtn = document.getElementById('chatNew');
        var greeted = false;
        var busy = false;

        var AVATAR = '<span class="oa-avatar oa-avatar-sm"><svg class="oa-face" width="18" height="18" aria-hidden="true"><use href="#botFace"></use></svg></span>';

        function addMessage(text, who) {
            var row = document.createElement('div');
            row.className = 'oa-row ' + (who === 'user' ? 'oa-row-user' : 'oa-row-bot');
            if (who !== 'user') { row.innerHTML = AVATAR; }
            var bubble = document.createElement('div');
            bubble.className = 'oa-msg ' + (who === 'user' ? 'oa-user' : 'oa-bot');
            bubble.textContent = text;
            row.appendChild(bubble);
            log.appendChild(row);
            log.scrollTop = log.scrollHeight;
            return bubble;
        }

        function remember(open) {
            try { sessionStorage.setItem('oaOpen', open ? '1' : '0'); } catch (e) { }
        }

        function openPanel(byUser) {
            panel.classList.add('open');
            launcher.setAttribute('aria-expanded', 'true');
            if (!greeted) { greeted = true; addMessage(GREETING, 'bot'); }
            if (byUser) { remember(true); input.focus(); }
        }

        function closePanel(byUser) {
            panel.classList.remove('open');
            launcher.setAttribute('aria-expanded', 'false');
            if (byUser) { remember(false); launcher.focus(); }
        }

        function resetChat() {
            log.innerHTML = '';
            greeted = true;
            addMessage(GREETING, 'bot');
            chips.style.display = 'flex';
            input.value = '';
            input.focus();
        }

        function ask(question) {
            question = question.trim();
            if (!question || busy) { return; }

            busy = true;
            sendBtn.disabled = true;
            chips.style.display = 'none';
            addMessage(question, 'user');
            input.value = '';

            var pending = addMessage('Typing...', 'bot');
            pending.classList.add('oa-pending');

            fetch('ChatServlet', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
                body: 'question=' + encodeURIComponent(question)
            }).then(function (res) {
                if (res.status === 401) { throw new Error('session'); }
                if (!res.ok) { throw new Error('server'); }
                return res.text();
            }).then(function (text) {
                pending.textContent = text;
            }).catch(function (err) {
                pending.textContent = (err && err.message === 'session')
                    ? 'Your session has expired. Please log in again.'
                    : "The assistant couldn't answer right now. Please try again.";
            }).then(function () {
                pending.classList.remove('oa-pending');
                busy = false;
                sendBtn.disabled = false;
                input.focus();
                log.scrollTop = log.scrollHeight;
            });
        }

        launcher.addEventListener('click', function () {
            if (panel.classList.contains('open')) { closePanel(true); } else { openPanel(true); }
        });
        minBtn.addEventListener('click', function () { closePanel(true); });
        newBtn.addEventListener('click', resetChat);
        sendBtn.addEventListener('click', function () { ask(input.value); });
        input.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') { e.preventDefault(); ask(input.value); }
        });
        chips.addEventListener('click', function (e) {
            var chip = e.target.closest('button[data-q]');
            if (chip) { ask(chip.getAttribute('data-q')); }
        });
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape' && panel.classList.contains('open')) { closePanel(true); }
        });

        // Starting state
        var saved = null;
        try { saved = sessionStorage.getItem('oaOpen'); } catch (e) { }
        var wide = window.matchMedia('(min-width: 900px)').matches;
        var startOpen = saved === '1' || (saved === null && OPEN_BY_DEFAULT);
        if (wide && startOpen) { openPanel(false); }
    })();
    </script>
</body>
</html>

