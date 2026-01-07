# Implementation Plan - Finance & Tasks Enhancements

## User Objectives
1. **Finance Dashboard Fix**: ensure the top dashboard updates with the date range filter.
2. **Lent Amount Visibility**: Ensure individual "due" amounts are visible.
3. **Delete Person**: Add option to delete a person from the finance section.
4. **Quick Add Person**: Add a person directly from the "Add Transaction" dialog.
5. **Task Sorting**: Sort tasks by time, not by creation order.

## Changes Implemented

### 1. Finance Dashboard Logic
- **File**: `app/src/main/java/com/example/lifeos/ui/finance/FinanceScreen.kt`
- **Change**: Updated the "Simplified Balance Summary" calculation to use `filteredTransactions` (which respects `startDate` and `endDate`) instead of `transactions` (all time).

### 2. Person Management
- **File**: `app/src/main/java/com/example/lifeos/ui/finance/FinanceScreen.kt`
    - Added `onDelete` callback to `PeopleList` and `PersonItem`.
    - Added a "Delete" icon button to `PersonItem`.
- **File**: `app/src/main/java/com/example/lifeos/ui/finance/FinanceViewModel.kt`
    - Added `deletePerson` function calling repository.

### 3. Quick Add Person
- **File**: `app/src/main/java/com/example/lifeos/ui/finance/FinanceScreen.kt`
    - In `AddTransactionDialog`, added a new item "Add New Person..." to the person dropdown.
    - Wired this action to open the `AddPersonDialog`.

### 4. Task Sorting
- **File**: `app/src/main/java/com/example/lifeos/ui/tasks/TasksViewModel.kt`
    - Modified `allTasks` flow to sort tasks by `startTime` and then `endTime`.

### 5. Bug Fixes
- **File**: `app/src/main/java/com/example/lifeos/ui/finance/FinanceScreen.kt`
    - Fixed `TransactionEntity` creation in `AddTransactionDialog` to use `RecoveryStatus.NONE` instead of `null` to match the non-nullable entity definition.

## Verification
- Built the project successfully (`assembleDebug`).
- Confirmed logic correctness through code review.
