package com.demo.application.core.application.di.component;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;
import com.demo.application.home.presentation.viewmodel.AuroScholarDashBoardViewModel;
import com.demo.application.home.presentation.viewmodel.CardViewModel;
import com.demo.application.home.presentation.viewmodel.CongratulationsDialogViewModel;
import com.demo.application.home.presentation.viewmodel.DemographicViewModel;
import com.demo.application.home.presentation.viewmodel.FriendsInviteViewModel;
import com.demo.application.home.presentation.viewmodel.FriendsLeaderShipViewModel;
import com.demo.application.home.presentation.viewmodel.GradeChangeViewModel;
import com.demo.application.home.presentation.viewmodel.HomeViewDashBoardModel;
import com.demo.application.home.presentation.viewmodel.HomeViewModel;
import com.demo.application.home.presentation.viewmodel.IntroScreenViewModel;
import com.demo.application.home.presentation.viewmodel.InviteFriendViewModel;
import com.demo.application.home.presentation.viewmodel.KYCViewModel;
import com.demo.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.demo.application.home.presentation.viewmodel.OtpScreenViewModel;
import com.demo.application.home.presentation.viewmodel.QuizTestViewModel;
import com.demo.application.home.presentation.viewmodel.QuizViewModel;
import com.demo.application.home.presentation.viewmodel.QuizViewNewModel;
import com.demo.application.home.presentation.viewmodel.ScholarShipViewModel;
import com.demo.application.home.presentation.viewmodel.SplashScreenViewModel;
import com.demo.application.home.presentation.viewmodel.TransactionsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    /*Home Module*/
    private HomeUseCase homeUseCase;
    private HomeDbUseCase homeDbUseCase;
    private HomeRemoteUseCase homeRemoteUseCase;




    public ViewModelFactory(Object objectOne, Object objectTwo, Object objectThree) {

        if (objectOne instanceof HomeUseCase && objectTwo instanceof HomeDbUseCase && objectThree instanceof HomeRemoteUseCase) {
            this.homeUseCase = (HomeUseCase) objectOne;
            this.homeDbUseCase = (HomeDbUseCase) objectTwo;
            this.homeRemoteUseCase = (HomeRemoteUseCase) objectThree;
        }

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewDashBoardModel.class)) {

            return (T) new HomeViewDashBoardModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }else if (modelClass.isAssignableFrom(CardViewModel.class)) {

            return (T) new CardViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(SplashScreenViewModel.class)){

            return (T) new SplashScreenViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(IntroScreenViewModel.class)){

            return (T) new IntroScreenViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(LoginScreenViewModel.class)){

            return (T) new LoginScreenViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(AuroScholarDashBoardViewModel.class)){

            return (T) new AuroScholarDashBoardViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(OtpScreenViewModel.class)){

            return (T) new OtpScreenViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if(modelClass.isAssignableFrom(GradeChangeViewModel.class)){

            return (T) new GradeChangeViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);

        }else if (modelClass.isAssignableFrom(QuizViewModel.class)) {

            return (T) new QuizViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(KYCViewModel.class)) {

            return (T) new KYCViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }
        else if (modelClass.isAssignableFrom(ScholarShipViewModel.class)) {

            return (T) new ScholarShipViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(DemographicViewModel.class)) {

            return (T) new DemographicViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(QuizTestViewModel.class)) {

            return (T) new QuizTestViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        }  else if (modelClass.isAssignableFrom(FriendsLeaderShipViewModel.class)) {

            return (T) new FriendsLeaderShipViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(FriendsInviteViewModel.class)) {

            return (T) new FriendsInviteViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(InviteFriendViewModel.class)) {

            return (T) new InviteFriendViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);

        } else if (modelClass.isAssignableFrom(CongratulationsDialogViewModel.class)) {

            return (T) new CongratulationsDialogViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);
        } else if (modelClass.isAssignableFrom(TransactionsViewModel.class)) {

            return (T) new TransactionsViewModel(homeUseCase, homeDbUseCase, homeRemoteUseCase);
        } else if(modelClass.isAssignableFrom(QuizViewNewModel.class)){

            return (T) new QuizViewNewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);
        }else if(modelClass.isAssignableFrom(HomeViewModel.class)){

            return (T) new HomeViewModel(homeUseCase,homeDbUseCase,homeRemoteUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}


