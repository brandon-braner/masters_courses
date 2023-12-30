import pytest
from .insertion_sort import insertion_sort


@pytest.mark.parametrize(
    "test_input, expected",
    [
        ([1, 2, 3, 4, 5], [1, 2, 3, 4, 5]),  # Already sorted list
        ([5, 4, 3, 2, 1], [1, 2, 3, 4, 5]),  # Reverse sorted list
        ([3, 1, 4, 5, 2], [1, 2, 3, 4, 5]),  # Unsorted list
        ([1], [1]),  # Single element list
        (
            [3, 1, 2, 3, 2],
            [1, 2, 2, 3, 3],
        ),  # List with duplicate values
        (
            [-3, -1, -2, 0],
            [-3, -2, -1, 0],
        ),  # List with negative values
        ([3, -1, 0, 5, 2], [-1, 0, 2, 3, 5]),  # List with mixed values
    ],
)
def test_insertion_sort_happy_path(test_input, expected):
    # Act
    result = insertion_sort(test_input)

    # Assert
    assert result == expected, f"Failed on {test_input}"


# Parametrized test for edge cases
@pytest.mark.parametrize(
    "test_input, expected",
    [
        ([], []),  # Empty list
    ],
)
def test_insertion_sort_edge_cases(test_input, expected):
    # Act
    result = insertion_sort(test_input)

    # Assert
    assert result == expected, f"Failed on {test_input}"
