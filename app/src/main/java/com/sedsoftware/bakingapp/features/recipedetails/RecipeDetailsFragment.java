package com.sedsoftware.bakingapp.features.recipedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.sedsoftware.bakingapp.R;
import com.sedsoftware.bakingapp.data.model.Ingredient;
import com.sedsoftware.bakingapp.data.model.Step;
import com.sedsoftware.bakingapp.features.recipestep.RecipeStepActivity;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsContract.View {

  @BindView(R.id.recipe_details_ingredients)
  TextView recipeDetailsIngredients;
  @BindView(R.id.recipe_details_steps)
  RecyclerView recipeDetailsRecyclerView;
  Unbinder unbinder;
  private RecipeDetailsContract.Presenter recipeDetailsPresenter;

  private RecipeDetailsAdapter recipeDetailsAdapter;
  private int recipeId;

  public RecipeDetailsFragment() {
  }

  public static RecipeDetailsFragment newInstance(int recipeId) {
    Bundle arguments = new Bundle();
    arguments.putInt(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipeId);
    RecipeDetailsFragment fragment = new RecipeDetailsFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    recipeId = getArguments().getInt(RecipeDetailsActivity.EXTRA_RECIPE_ID);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
    unbinder = ButterKnife.bind(this, view);

    recipeDetailsAdapter = new RecipeDetailsAdapter(new ArrayList<>(0),
        stepId -> recipeDetailsPresenter.openStepDetails(stepId));

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    recipeDetailsRecyclerView.setLayoutManager(layoutManager);
    recipeDetailsRecyclerView.setHasFixedSize(true);
    recipeDetailsRecyclerView.setAdapter(recipeDetailsAdapter);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    recipeDetailsPresenter.subscribe();
  }

  @Override
  public void onPause() {
    super.onPause();
    recipeDetailsPresenter.unsubscribe();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void setPresenter(RecipeDetailsContract.Presenter presenter) {
    this.recipeDetailsPresenter = presenter;
  }

  @Override
  public void showIngredientsList(List<Ingredient> ingredients) {
    recipeDetailsIngredients.setText("");
    for (Ingredient ingredient : ingredients) {
      recipeDetailsIngredients.append(ingredient.ingredient());
      recipeDetailsIngredients.append("\n");
    }
  }

  @Override
  public void showStepsList(List<Step> steps) {
    recipeDetailsAdapter.refreshStepsList(steps);
  }

  @Override
  public void showErrorMessage() {
    // TODO(2) Implement this (perhaps not used)
  }

  @Override
  public void showStepDetails(int stepId) {
    startActivity(RecipeStepActivity.prepareIntent(getContext(), recipeId, stepId));
  }
}
