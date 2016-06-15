(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Task', 'Unit'];

    function TaskDetailController($scope, $rootScope, $stateParams, entity, Task, Unit) {
        var vm = this;

        vm.task = entity;

        var unsubscribe = $rootScope.$on('jhipsterApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
