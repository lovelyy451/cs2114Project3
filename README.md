
# Purrfect Fit — Group 37

**CS 2114 | Project 3 | Deliverable 4 — Second Prototype + Tests**

**Team:** Lovesh Kataria (Model), Amelia Vasquez Rosario (View), Ari Bajaj (Controller)

## Overview

Purrfect Fit is a Java boutique game where players interpret customer dialogue and select the correct hat, top, and shoes from three choices per category.

A custom singly-linked FIFO queue manages three waiting customers. Correct outfits earn 10 points, and players must complete the required number of outfits before the level timer expires. The game contains 15 progressively harder levels.

The application follows the Model-View-Controller (MVC) architecture.

## How to Run

### Model and Tests

The Model is in `purrfectfit.model` and runs independently of the GUI.

Run the JUnit tests using the project's configured test runner or Maven:

`mvn test`

The tests cover the Queue, customer generation, clothing, outfits, dialogue, scoring, timing, and progression.

**Test status:** All Model tests pass with zero failures and zero errors. `LinkedQueueTest` also verifies FIFO order after interleaved enqueue and dequeue operations.

### View and Controller

The View is in purrfectfit.view (or, purrfectfit/view if you consider the folders)
and it's dependent on the Controller, as the Controller communicates with the
Model and translates that information for the View to display.

The View can be run/started up through Maven via:

`mvn exec:java`

---

## Reflection

### Changes From Specification

| Area | Specification → Implementation | Reason |
|---|---|---|
| Level progression | General `startLevel(int)` → Level 1 only; `retryLevel()` and `nextLevel()` handle subsequent transitions | Prevent unauthorized progression |
| Outfit validation | Check correctness → Also reject items outside displayed options | Enforce rules independently of GUI |
| Incomplete submissions | Reject incomplete outfits → Constructor validates items; `submitOutfit(null)` returns `INVALID` | Protect game state |
| Timing | Model-owned timer → Injectable `LongSupplier` clock, preserved final time, dialogue-time exclusion | Deterministic tests and fair gameplay |
| Dialogue | AI and fallback generators planned → Fallback implemented; external AI dropped | API costs and unreliable adherence to dialogue constraints |
| Hints | AI-generated hints planned → Fallback-based hints retained | Avoid external dependencies |
| Tests | Planned test cases → Passing JUnit tests across Model classes | Verify logic without the View |
| Methods | Needed to add a few extra methods for Controller classes | Handle input and output separately |

The implementation also standardizes the outfit to three categories (`HAT`, `TOP`, `SHOES`) and uses three displayed options per category, resolving inconsistencies in the specification.

### Non-Trivial Feature: Dialogue and Hints

The D3 plan proposed AI-generated customer dialogue with two additional hint requests per level.

External AI integration was dropped because the API required paid access and testing showed inconsistent adherence to dialogue constraints. After approximately 50–100 conversational test attempts, responses sometimes exceeded character limits or failed to follow the required conversation boundaries.

Instead, the project retained fallback dialogue for hints, avoiding external API dependencies.

### Model/View Separation

The Model has no Swing dependencies. `GameModel` owns gameplay rules, timing, scoring, and the customer queue. `LinkedQueue<T>` handles FIFO operations.

The Controller is responsible for passing player actions to the Model and updating the View. Model tests run without constructing GUI components.

Amelia initially wanted to have the swing timer implemented in the view, but
upon further consideration the controller now has it implemented. As such,
the controller now has a swing dependency.

The controller also utilizes methods present within the view to pass back and
verify with the model, and such is the loop.

The fallback dialogue system allows dialogue generation without changing core gameplay logic. No additional Model classes or method signatures were changed from D3.

The original prediction that Queue, clothing, and outfit classes would remain unchanged was maintained. The main difference from the D3 plan was dropping external AI integration.

A future improvement would be to organize fallback dialogue into personality-specific collections, allowing new dialogue to be added without changing gameplay logic.

### Unknowns From Scope

| Owner | Original unknown | Current status |
|---|---|---|
| Lovesh | Separating Model and View | Model implemented independently; integrated |
| Amelia | Customer visualization, music, visible timer | sample implemented. |
| Ari | Timer functions and customer randomization | Timer and customer randomization logic implemented easily |

### Missing or Unverified Functionality

- External AI dialogue was dropped; fallback dialogue is available.
- Missing hint button and generated hints.
- All Model tests pass.
- No known Model bugs or failing tests remain.
- Fallback dialogue could be expanded with personality-specific responses and more randomized customer names.
- Missing some digitally hand-drawn visuals/images, fonts, mand music.
- Whether the cat bases are independent of the personalities, and if they aren't, they must become independent of the personalities and randomize accordingly.

---

## Next Steps

### Remaining MVP Work

| Priority | Task | Owner |
|---|---|---|
| 1 | Finish minimal playable View | Amelia |
| 2 | Connect View and Model through Controller | Ari |
| 3 | Integrate and test full gameplay | Everyone |
| 4 | Test bad inputs and resolve crashes | Everyone |
| 5 | Expand fallback dialogue with personality-specific responses | Lovesh |
| 6 | Add a larger randomized customer-name collection | Lovesh |
| 7 | Test new dialogue and name-generation behavior | Lovesh |
| 8 | Finalize README and submission | Everyone |

### D4 Feature Decision: AI Dialogue and Limited Hint Requests

**Original proposed feature:** AI-generated dialogue with two additional hint requests per level.

**D4 decision:** External AI integration was dropped because of API costs and inconsistent responses during testing. Fallback dialogue remains available for hints.

**D3 predicted changes:**

| Class | Specific changes |
|---|---|
| `GameModel` | Add `remainingHints`; modify `initializeLevel()` to reset it; add `requestHint()` and `getRemainingHints()` |
| `CustomerGenerator` | Add `generateAdditionalHint(Customer)` and reuse fallback handling |
| `AIDialogueGenerator` | Implement `generateDialogue(Customer)` and additional-hint generation |
| `Customer` | Add hint history if required to avoid duplicate clues |
| `FallBackDialogueGenerator` | Add fallback support for additional hints |
| `InputHandler` | Add an `onHintRequested()` action |
| `OutputHandler` | Update `refreshGame()` to display hint state |
| `GameScreen` | Add a hint button and remaining-hints display |

**Expected unchanged classes:** `LinkedQueue`, `ClothingItem`, `ClothingCategory`, `Outfit`, `ClothingCatalog`, `Personality`, `GameStatus`, and `SubmissionResult`.

**Actual Model changes:** No additional Model classes or method signatures were changed from D3. External AI integration was dropped, and fallback dialogue was retained.

**Owners:** Lovesh — Model and tests; Ari — Controller; Amelia — View.

**Status:** External AI dropped; fallback dialogue and hints retained.

### Stretch Goals

- **Keep:** Expanded personality-specific fallback dialogue and randomized customer names.
- **Drop:** External AI integration, monetary system, tips, purchasable decorations, and upgrades.

### Final Build Tests

**Model — Lovesh**
- All existing Model JUnit tests pass.
- `LinkedQueueTest` verifies FIFO order after interleaved enqueue and dequeue operations.
- Test any newly added personality-specific dialogue.
- Test expanded randomized customer-name generation.
- Verify Model behavior during final integration.

**Controller — Ari:** Test hint-button actions, repeated clicks, and rejected requests.

**View — Amelia:** Test hint display, remaining count, and failure feedback.

**Integration — Everyone:** Test the complete MVP and all bad inputs from the scope: incomplete outfits, incorrect selections, repeated submissions, expired submissions, and attempts to bypass FIFO order.

### Final Demo — Model Contribution

The three-minute demo will demonstrate normal gameplay, including customer dialogue, outfit submission, scoring, and fallback-based hint requests.

The Model portion will demonstrate that gameplay functions without external AI services.

---

## Attribution

### Model Source Files

| File | Contributor | Contribution |
|---|---|---|
| `GameModel.java` | Lovesh | Developed, revised, tested |
| `LinkedQueue.java` | Lovesh | Developed, revised, tested |
| `ClothingItem.java` | Lovesh | Developed, tested |
| `ClothingCategory.java` | Lovesh | Developed, tested |
| `Outfit.java` | Lovesh | Developed, tested |
| `ClothingCatalog.java` | Lovesh | Developed, tested |
| `Customer.java` | Lovesh | Developed, tested |
| `CustomerGenerator.java` | Lovesh | Developed, tested |
| `Personality.java` | Lovesh | Developed, tested |
| `DialogueGenerator.java` | Lovesh | Developed, reviewed |
| `FallBackDialogueGenerator.java` | Lovesh | Developed, tested |
| `GameStatus.java` | Lovesh | Developed, tested |
| `SubmissionResult.java` | Lovesh | Developed, tested |

### Model Test Files

| File | Contributor | Contribution |
|---|---|---|
| `GameModelTest.java` | Lovesh | Developed and tested |
| `LinkedQueueTest.java` | Lovesh | Developed and tested |
| `ClothingItemTest.java` | Lovesh | Developed and tested |
| `ClothingCategoryTest.java` | Lovesh | Developed and tested |
| `OutfitTest.java` | Lovesh | Developed and tested |
| `ClothingCatalogTest.java` | Lovesh | Developed and tested |
| `CustomerTest.java` | Lovesh | Developed and tested |
| `CustomerGeneratorTest.java` | Lovesh | Developed and tested |
| `PersonalityTest.java` | Lovesh | Developed and tested |
| `FallBackDialogueGeneratorTest.java` | Lovesh | Developed and tested |
| `GameStatusTest.java` | Lovesh | Developed and tested |
| `SubmissionResultTest.java` | Lovesh | Developed and tested |

### View Source and Test Files

| File | Contributor | Contribution |
|---|---|---|
| `MainWindow.java` | Amelia | Developed and tested |
| `GameScreen.java` | Amelia | Developed and tested |
| `StartScreen.java` | Amelia | Developed and tested |
| `ResultsScreen.java` | Amelia | Developed and tested |
| `ScenePanel.java` | Amelia | Developed and tested |
| `CategoryPanel.java` | Amelia | Developed and tested |
| `Assets.java` | Amelia | Developed |
| `Fonts.java` | Amelia | Developed |
| `Music.java` | Amelia | Developed |

### Controller Source and Test Files

| File | Contributor | Contribution |
|---|---|---|
| `InputHandler.java` | Ari | Developed and tested |
| `OutputHandler.java` | Ari | Developed and tested |
| `LevelLoader.java` | Ari | Developed and tested |
| `Game.java` | Ari | Developed and tested |

### GenAI Notes

**Lovesh:** Used GenAI to make sure code is consistent with each other and everything in rubric is followed.

**Amelia:** Used GenAI to help with the creation of temporary simple view files
in order for the first Prototype to be visually assessed through the view.
For our next prototype, I'll have reworked the code to properly reflect my/our
desired game graphical representation, as well as implement the designs I'm
currently working on using Procreate (stills) and potentially ToonSquid (gifs).

**Ari:** I did not use GenAI.

---

## D4 Verification

| Requirement | Status |
|---|---|
| Independent Queue-backed Model | Implemented |
| Passing Model and Queue JUnit tests | Confirmed passing |
| Interleaved enqueue/dequeue FIFO test | Implemented |
| Known Model bugs | None reported |
| External AI integration | Dropped; explained in Reflection |
| Fallback-based hints | Retained |
| View through Controller | Reported passing in D4 |
| MVP runs without crashing | Reported passing in D4 |
| Scope bad-input cases survive | Integrated and passing |
| Reflection and final build plan | Documented |
| Complete file attribution | Retained from D3 |
| GenAI notes | Completed |
