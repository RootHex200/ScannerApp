
### Architecture 
MVVM - MVVM stands for Model, View, ViewModel. MVVM is one of the architectural patterns which enhances separation of concerns,

![image](https://github.com/user-attachments/assets/55d3f624-f8cf-43d1-a463-f970167aa37b)

### Directory Structure

```
├── core
│   ├── App.kt
│   ├── base
│   │   ├── BaseActivity.kt
│   │   ├── BaseAdapter.kt
│   │   ├── BaseFragment.kt
│   │   ├── BaseViewModel.kt
│   │   ├── SimpleActivity.kt
│   │   └── SimpleFragment.kt
│   ├── common
│   │   ├── RequestCompleteListener.kt
│   │   └── model
│   │       └── QrScanValueModel.kt
│   ├── db
│   │   ├── AppDatabase.kt
│   │   ├── QRHistoryDao.kt
│   │   └── QrCodeInfo.kt
│   └── di
│       ├── ViewModelFactory.kt
│       ├── component
│       │   └── ApplicationComponent.kt
│       ├── key
│       │   └── ViewModelKey.kt
│       └── module
│           ├── AppInjector.kt
│           ├── DataSourceModule.kt
│           ├── DatabaseModule.kt
│           ├── Injectable.kt
│           ├── QrRepositoryModule.kt
│           ├── QrServiceModule.kt
│           └── ViewModelModule.kt
├── data
│   ├── data_source
│   │   ├── QrCodeDataSource.kt
│   │   └── QrCodeLocalDataSource.kt
│   └── repositories
│       └── QrCodeRepositoryImpl.kt
├── domain
│   ├── model
│   │   └── QrCode.kt
│   └── repositories
│       └── QrCodeRepositories.kt
├── service
│   ├── QrGeneratorServiceImpl.kt
│   ├── QrGeneratorServices.kt
│   ├── QrScannerService.kt
│   └── QrScannerServiceImpl.kt
└── view
    ├── details
    │   ├── DetailsActivity.kt
    │   └── viewmodel
    │       └── DetailsViewModel.kt
    ├── landing
    │   ├── QrGenerator
    │   │   ├── QRgenerator.kt
    │   │   ├── QrGeneratorDetails
    │   │   │   └── QrGeneratorDetailsActivity.kt
    │   │   ├── QrGeneratorOption
    │   │   │   └── TextQrFragment.kt
    │   │   ├── adapter
    │   │   │   └── QrGeneratorOptionAdapter.kt
    │   │   ├── model
    │   │   │   └── QrOptionItem.kt
    │   │   └── viewmodel
    │   │       └── QrGeneratorViewModel.kt
    │   ├── bottom_navigation.kt
    │   ├── history
    │   │   ├── QRhistory.kt
    │   │   ├── adapter
    │   │   │   └── ScanHistoryListAdapter.kt
    │   │   ├── createHistory
    │   │   │   └── createHistory.kt
    │   │   ├── scanHistory
    │   │   │   └── ScanHistory.kt
    │   │   └── viewmodel
    │   │       └── HistoryViewModel.kt
    │   └── scanner
    │       ├── QRscanner.kt
    │       └── ScannerViewModel.kt
    └── onboarding
        └── OnboardingActivity.kt

```
