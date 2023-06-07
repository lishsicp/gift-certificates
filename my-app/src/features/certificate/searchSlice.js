import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  size: 10,
  name_sort: null,
  date_sort: "desc",
};

const searchSlice = createSlice({
  name: "search",
  initialState,
  reducers: {
    resetSearch: (state) => {
      state.name_sort = initialState.name_sort;
      state.date_sort = initialState.date_sort;
    },
    updateSize: (state, action) => {
      state.size = action.payload;
    },
    updateDateSort: (state, action) => {
      state.date_sort = action.payload;
    },
    updateNameSort: (state, action) => {
      state.name_sort = action.payload;
    },
  },
});

export default searchSlice.reducer;

export const { resetSearch, updateSize, updateDateSort, updateNameSort } =
  searchSlice.actions;
