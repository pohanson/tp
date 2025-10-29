---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `SidebarPanel`, `TemplateViewPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

**New UI Components in OnlySales:**
* `ImportWindow` - A separate window for importing customer data from clipboard
* `SidebarPanel` - A panel that contains the `StatusViewPanel` and `TagsViewPanel` for displaying active filters
* `StatusViewPanel` - Displays all currently active status filters applied through the find command
* `TagsViewPanel` - Displays all currently active tag filters applied through the find command  
* `TemplateViewPanel` - Displays and manages email templates for different customer status types

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` objects residing in the `Model`.
* The `SidebarPanel` contains nested UI components (`StatusViewPanel` and `TagsViewPanel`) that update automatically when filter commands are executed.
* The `ImportWindow` interacts with the `Logic` component to process imported customer data.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the new `Logic` component, with additional classes to the AB3 being shown in light blue:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Status View and Tag View Feature

#### Overview

The Status View and Tag View feature provides visual feedback to users about which filters are currently active when using the `find` command. When users search for customers by status (e.g., `find s:Contacted`) or tags (e.g., `find t:friends`), dedicated UI panels automatically update to display the active filters, making it easy to see what subset of data is being viewed.

#### Architecture

The implementation follows the **Observer Pattern** using JavaFX's property binding mechanism to automatically sync UI state with model state.

![Status/Tag View Class Diagram](images/StatusTagViewClassDiagram.png)

**Key Components:**

1. **Model Layer:**
   - `StatusViewState` and `TagsViewState`: Immutable state objects that represent current filter states
   - `ModelManager`: Stores these states as `ObjectProperty` objects and exposes them via the `Model` interface

2. **Logic Layer:**
   - `Logic` interface: Exposes `getStatusViewStateProperty()` and `getTagsViewStateProperty()` methods
   - `FindCommand`: Updates the view states in `Model` when executing filter operations

3. **UI Layer:**
   - `StatusViewPanel` and `TagsViewPanel`: Observe the properties exposed by `Logic` and automatically update their display
   - UI components depend on the `Logic` abstraction, maintaining proper architectural layering

#### Implementation Details

The following sequence diagram shows how the view states are updated when a user executes `find s:Contacted t:friends`:

![Status/Tag View Sequence Diagram](images/StatusTagViewSequenceDiagram.png)

**Step-by-step flow:**

1. User executes a `find` command with status/tag filters
2. `LogicManager` parses and creates a `FindCommand`
3. `FindCommand.execute()` is called:
   - Updates the filtered person list in `Model`
   - Calls `model.setStatusViewState()` with the appropriate `StatusViewState`
   - Calls `model.setTagsViewState()` with the appropriate `TagsViewState`
4. `ModelManager` updates its `ObjectProperty` fields
5. JavaFX property listeners in `StatusViewPanel` and `TagsViewPanel` are automatically triggered
6. UI panels update their labels to display the active filters

#### Design Considerations

**Aspect: How to represent filter state in the UI**

* **Alternative 1 (Chosen):** Use explicit state objects (`StatusViewState`, `TagsViewState`) to track user intent
  * Pros: UI displays what the user explicitly searched for (intent), not just the consequence. Handles edge cases where multiple filter combinations produce the same result. Clear separation of concerns.
  * Cons: Additional state management complexity, requires synchronization between filter predicates and view states

* **Alternative 2:** Derive view state from `FilteredPersonList`
  * Pros: Single source of truth, no state synchronization needed, simpler implementation
  * Cons: UI displays consequence rather than intent. For example, if a user searches for `s:Contacted` but no customers have that status, the filtered list would be empty and the UI couldn't distinguish whether filters were applied or not. Cannot accurately determine which specific filters were applied if multiple filter combinations produce the same filtered list.

**Aspect: How to communicate filter state to UI**

* **Alternative 1:** Direct UI method calls from Command classes
  * Pros: Simpler to understand, explicit control flow
  * Cons: Violates architectural boundaries (Logic calling UI directly) and tight coupling!!

**Aspect: Where to store view state**

* **Alternative 1 (Chosen):** Store in `Model` layer
  * Pros: Centralized state management, follows MVC pattern, testable
  * Cons: Model becomes slightly more complex

* **Alternative 2:** Store in UI components only
  * Pros: Simpler Model layer
  * Cons: State is scattered, harder to test, UI must deduce state from filtered list



### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n:David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n:David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

- Salespersons who manage a large number of leads/contacts
- Prefer desktop apps over other types
- Can type fast and prefer typing to mouse interactions for efficiency
- Need to categorize leads by tags and track sales-specific statuses (e.g., Contacted, Rejected, Accepted)
- Occasionally need to import many contacts at once (e.g., from sales manager assignments)
- Are reasonably comfortable using CLI apps

**Value proposition**: An address book tailored for salespeople to manage contacts significantly faster than a typical mouse/GUI driven app, with support for bulk additions, powerful filtering by name/tag/status, status tracking to streamline outreach workflows, and templated email generation for selected contact cohorts.

**Key features**:

- Add Contact: Add single or multiple contacts in one command
- Edit Contact: Update any field, add/remove tags, set status
- Delete Contact: Remove contacts by index
- List Contact: Display all contacts
- Find Contact: Filter by name, tag, and/or status (case-insensitive, exact match)
- Create Email Template: Generate email templates for selected tag/status cohorts
- Set Status: Quickly update a contact's status

**Command format conventions**:

- Named parameters use `key:value` format and may appear in any order
- Optional parameters are denoted with square brackets `[]`
- Repeating parameters are denoted with `...`
- Leading/trailing whitespace is trimmed for all fields
- Each named parameter continues till the end of the line or till another parameter

**Field validations**:

- Phone number: Must contain only digits and `+`
- Email: Must contain `@` symbol
- Status: Must be one of the valid statuses (Contacted, Rejected, Accepted, Unreachable, Busy, Uncontacted)
- Name and Address: No validation to allow flexibility

**Valid contact statuses**:

- **Uncontacted**: Default status when a contact is added (indicates not yet contacted)
- **Contacted**: Contact has been contacted
- **Rejected**: Contact has rejected the sale
- **Accepted**: Contact has accepted the sale
- **Unreachable**: Contact could not be reached
- **Busy**: Contact is busy and should be contacted later

### User stories

Priorities: Essential (must have) - `* * *`, Typical (nice to have) - `* *`, Novel (unlikely to have) - `*`

| Priority | As a …               | I want to …                                                        | So that I can…                                                            |
| -------- | -------------------- | ------------------------------------------------------------------ | ------------------------------------------------------------------------- |
| `* * *`  | salesperson          | add contacts                                                       | see their details in the future                                           |
| `* * *`  | salesperson          | add multiple contacts from the team IC easily                      | see their details in the future                                           |
| `* * *`  | salesperson          | delete contacts                                                    | don't over clutter my contact book                                        |
| `* *`    | salesperson          | delete multiple contacts                                           | ensure that PDPA retention limitation is adhered to                       |
| `* * *`  | careless salesperson | edit typos                                                         | ensure the data is accurate                                               |
| `* *`    | salesperson team IC  | export and share the contacts I have with others easily            | don't need my team to use each others' accounts                           |
| `*`      | lazy salesperson     | add contacts from .vcf files                                       | don't need to add existing contacts one-by-one                            |
| `*`      | lazy salesperson     | copy a preformatted message based on certain tags the user has     | it sounds personalised                                                    |
| `*`      | salesperson          | create templates that can be sent to a particular type of customer | save time from writing up the same outreach materials over and over again |
| `*`      | salesperson team IC  | edit a preformatted message                                        | edit the message when needed                                              |
| `* *`    | salesperson          | add labels to contacts                                             | categorise them for filtering                                             |
| `* * *`  | salesperson          | search by name                                                     | easily find by name due to the large number of contacts                   |
| `* *`    | salesperson          | search by labels                                                   | easily find by labels due to the large number of contacts                 |
| `* *`    | salesperson          | filter by labels                                                   | see all clients of each label                                             |
| `* * *`  | salesperson          | list all contacts                                                  | know what contacts I have saved                                           |
| `* * *`  | forgetful user       | autosave edits                                                     | data won't be lost if I forget to save it                                 |
| `*`      | salesperson          | tag each customer                                                  | better remember what preference the customer has                          |
| `*`      | salesperson          | quickly identify clients who have been called and rejected         | not waste time calling them again                                         |
| `*`      | busy salesperson     | quickly mark clients based on how receptive they are               | focus my limited time on those likely to buy the product                  |

### Use cases

#### Use case: UC01 - Add new contact<br/>

**System:** Contact Management System (CMS)

**Actor:** Salesperson<br/>

**Guarantees:**

* Contact is created only if all required fields are valid.
* On validation error, no contacts are added.

**MSS:**

1. Salesperson chooses to add a new contact.
2. Salesperson enters the add command with contact details.
3. Salesperson submits the command.
4. CMS validates the details.
5. CMS creates the contact and displays a confirmation message.<br/>
   Use case ends.

**Extensions:**<br/>

4a. CMS detects an error in the entered data.<br/>
   4a1. CMS indicates an error has happened.<br/>
   Use case resumes from step 2.


#### Use case: UC02 - Add multiple contacts

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* If any entry is invalid, none of the contacts are added.
* No existing contacts are modified by this operation.

**MSS:**

1. Salesperson chooses to import many contacts.
2. Salesperson enters the add command with multiple contacts separated by "|||", then submits the command.
3. CMS validates all entries.
4. CMS creates the contacts and displays a summary confirmation.<br/>
   Use case ends.

**Extensions:**<br/>

3a. CMS detects an error in the entered data.<br/>
   3a1. CMS indicates an error has happened.<br/>
   Use case resumes from step 2.


#### Use case: UC03 - List all contacts

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Listing does not modify any data.
* The latest saved state of contacts is displayed.

**MSS:**

1. Salesperson chooses to view all contacts.
2. Salesperson enters the list command.
3. CMS displays all contacts.<br/>
   Use case ends.

**Extensions:**

3a. The contact list is empty.<br/>
   3a1. CMS indicates that no contacts are found.
   Use case ends.


#### Use case: UC04 - Find contact by name

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Search does not modify contact data.

**MSS:**

1. Salesperson chooses to find a specific contact by name.
2. Salesperson enters the search command.
3. CMS searches for contacts whose names contain the keyword(s) (case-insensitive).
4. CMS displays the matching contacts.<br/>
   Use case ends.

**Extensions:**

3a. No contacts match the search criteria.<br/>
   3a1. CMS indicates that no contacts are found.<br/>
   Use case ends.


#### Use case: UC05 - Find contact by status

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Filtering does not modify contact data.

**MSS:**

1. Salesperson chooses to find contacts by status.
2. Salesperson specifies the status.
3. CMS searches for contacts with the specified status.
4. CMS displays the matching contacts.<br/>
   Use case ends.

**Extensions:**

2a. The specified status does not exist.<br/>
   2a1. CMS indicates that an error has happened.<br/>
   Use case ends.

4a. No contacts match the specified status.<br/>
   4a1. CMS indicates that no contacts are found.<br/>
   Use case ends.


#### Use case: UC06 - Create email template

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Generating a template or preview does not modify any contact records.
* If template input is canceled, no template content is saved or applied.

**MSS:**

1. Salesperson chooses to create an email template.
2. Salesperson specifies the target tag(s) and/or status.
3. CMS prompts for the email content.
4. Salesperson enters the email template and submits.
5. CMS saves the personalised email template.<br/>
   Use case ends.

**Extensions:**

*a. At any time during template input, Salesperson chooses to cancel his input.<br/>
   *a1. CMS requests confirmation of the cancellation.<br/>
   *a2. Salesperson confirms the cancellation.<br/>
   Use case ends.


#### Use case: UC07 - Edit contact

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Only the specified fields are updated, the other fields remain unchanged.
* On validation error, no changes are applied.

**MSS:**

1. Salesperson chooses to edit a contact's information.
2. Salesperson specifies the contact ID and fields to edit.
3. CMS validates the updated details.
4. CMS updates the contact information.<br/>
   Use case ends.

**Extensions:**

2a. The specified contact ID does not exist.<br/>
   2a1. CMS indicates that an error has happened.  <br/>
   Use case resumes from step 2.

3a. CMS detects an error in the entered data.<br/>
   3a1. CMS indicates that an error has happened.  <br/>
   Use case resumes from step 2.


#### Use case: UC08 - Delete contact

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* Deletion removes only the specified contact without modifying other data.
* Deletion is irreversible once confirmed.
* On failure, no deletion occurs.

**MSS:**

1. Salesperson chooses to delete a contact.
2. Salesperson specifies the contact ID to delete.
3. CMS deletes the contact and displays a confirmation.<br/>
   Use case ends.

**Extensions:**

3a. The given contact ID is invalid.<br/>
   3a1. CMS indicates that an error has happened.<br/>
   Use case resumes at step 2.

3b. CMS requests for confirmation before deletion.<br/>
   3b1. Salesperson confirms the deletion.<br/>
   3b2. CMS proceeds with deletion.<br/>
   Use case ends.


#### Use case: UC09 - Set contact status

**System:** Contact Management System (CMS)

**Actor:** Salesperson

**Guarantees:**

* If an invalid status is provided, no changes are made.

**MSS:**

1. Salesperson chooses to update a contact's status.
2. Salesperson specifies the contact ID and new status.
3. CMS validates the contact ID and status.
4. CMS updates the contact's status and displays a confirmation message.<br/>
   Use case ends.

**Extensions:**

2b. No status is specified.<br/>
   2b1. CMS indicates that an error has happened.<br/>
   Use case ends.

3a. The specified status is invalid.<br/>
   3a1. CMS indicates that an error has happened.<br/>
   Use case ends.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage, especially in things like returning search results or filtering by label.
3. A user with above average typing speed (>60 WPM) for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The commands should follow **consistent patterns** to reduce confusion and make it easier to learn them.
5. User data should be **automatically saved** after commands such that user data is not lost even after unexpected shutdowns.
6. The program should be able to be self-contained as a single JAR/ZIP file that should work without requiring any installer.
7. The GUI should _work well_ for standard screen resolutions 1920x1080 and higher, and, for screen scales 100% and 125%, meaning that no clipping or obvious bugs show in the GUI.
8. The GUI should be _usable_ (i.e., all functions can be used even if the user experience is not optimal) for resolutions 1280x720 and higher, and, for screen scales 150%.
9. There should not be a server component. All data should be stored on the user's local machine.

### Glossary

* **Case-insensitive**: Matching that ignores letter casing (e.g., John == john).
* **CLI**: Command-line interface used to interact with the CMS.
* **Command**: A textual instruction entered into the CLI (e.g., add, edit, list).
* **Contact**: People who would be contacted by the salesperson.
* **Email Template**: A reusable message body that can be personalized and applied to cohorts (by tag/status).
* **Filter**: Showing customers that match specific attributes (e.g., tag, status).
* **GUI**: Graphical user interface components that display results and lists.
* **Mainstream OS**: Windows, Linux, Unix, MacOS.
* **Priority**: A user-defined importance level to help triage outreach.
* **Salesperson**: People who would be using the app to contact.
* **Search**: Finding customers by name keywords (case-insensitive).
* **Status**: Sales process specific tag that would denote the outcome of contacting the contacts. The meaning of each status would be decided by the company. Will be one of Contacted, Rejected, Accepted, Unreachable, Busy, Uncontacted.
* **Tag**: A user-defined label used to categorize customers (e.g., productA).
* **User**: The people using the app, which is the salesperson.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Finding customers

1. Finding customers with various criteria such as name, tag, status, phone number and email!

   1. Prerequisites: List all customers using the `list` command. Multiple customers in the list with different attributes.

   1. Test case: `find n:John`<br>
      Expected: Customers with "John" in their name are displayed. Tag view and status view remain unchanged.

   1. Test case: `find s:Contacted`<br>
      Expected: Customers with "Contacted" status are displayed. Status view panel updates to show "Contacted" as active filter.

   1. Test case: `find t:friends s:Contacted`<br>
      Expected: Customers with "friends" tag AND "Contacted" status are displayed. Both tag view and status view show active filters.

   1. Test case: `find`<br>
      Expected: Error message indicating invalid command format.

   1. Other test cases to try: `find p:9876`, `find e:example.com`, `find n:alex david` (multiple keywords), `find s:Invalid` (invalid status)<br>
      Expected: Appropriate results or empty list!



### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
