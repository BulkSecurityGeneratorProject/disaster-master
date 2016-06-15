(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mission-status', {
            parent: 'entity',
            url: '/mission-status',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.missionStatus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission-status/mission-statuses.html',
                    controller: 'MissionStatusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('missionStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mission-status-detail', {
            parent: 'entity',
            url: '/mission-status/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.missionStatus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission-status/mission-status-detail.html',
                    controller: 'MissionStatusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('missionStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MissionStatus', function($stateParams, MissionStatus) {
                    return MissionStatus.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('mission-status.new', {
            parent: 'mission-status',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission-status/mission-status-dialog.html',
                    controller: 'MissionStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mission-status', null, { reload: true });
                }, function() {
                    $state.go('mission-status');
                });
            }]
        })
        .state('mission-status.edit', {
            parent: 'mission-status',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission-status/mission-status-dialog.html',
                    controller: 'MissionStatusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MissionStatus', function(MissionStatus) {
                            return MissionStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission-status', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mission-status.delete', {
            parent: 'mission-status',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission-status/mission-status-delete-dialog.html',
                    controller: 'MissionStatusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MissionStatus', function(MissionStatus) {
                            return MissionStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission-status', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
