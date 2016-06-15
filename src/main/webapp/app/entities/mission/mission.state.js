(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mission', {
            parent: 'entity',
            url: '/mission',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.mission.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission/missions.html',
                    controller: 'MissionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mission');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mission-detail', {
            parent: 'entity',
            url: '/mission/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.mission.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mission/mission-detail.html',
                    controller: 'MissionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mission');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Mission', function($stateParams, Mission) {
                    return Mission.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('mission.new', {
            parent: 'mission',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-dialog.html',
                    controller: 'MissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isAccepted: null,
                                name: null,
                                notes: null,
                                dateStarted: null,
                                dateCompleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('mission');
                });
            }]
        })
        .state('mission.edit', {
            parent: 'mission',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-dialog.html',
                    controller: 'MissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mission', function(Mission) {
                            return Mission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mission.delete', {
            parent: 'mission',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mission/mission-delete-dialog.html',
                    controller: 'MissionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mission', function(Mission) {
                            return Mission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mission', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
